package co.sena.cimm.adso.saludboyaca.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionTestHelper {

    private static HikariDataSource dataSource;
    private static boolean tablasCreadas = false;

    public static synchronized void inicializar() throws Exception {
        if (dataSource != null && !dataSource.isClosed()) {
            if (!tablasCreadas) crearEstructura();
            return;
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:saludboyaca_test;DB_CLOSE_DELAY=-1;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(10000);
        dataSource = new HikariDataSource(config);
        crearEstructura();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void ejecutarSQL(String sql) throws Exception {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static void limpiarTablas() throws Exception {
        inicializar();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            if (tablaExiste(conn, "CITAS"))        stmt.execute("DELETE FROM citas");
            if (tablaExiste(conn, "PACIENTES"))    stmt.execute("DELETE FROM pacientes");
            if (tablaExiste(conn, "USUARIOS"))     stmt.execute("DELETE FROM usuarios");
            if (tablaExiste(conn, "ESPECIALIDADES")) stmt.execute("DELETE FROM especialidades");
        }
        // Solo especialidades — datos de catálogo, no contaminan UsuarioDAOTest
        ejecutarSQL("INSERT INTO especialidades (id, nombre, descripcion) VALUES " +
            "(1, 'Medicina General', 'Atencion primaria')," +
            "(2, 'Odontologia', 'Salud oral')," +
            "(3, 'Pediatria', 'Atencion infantil')");
        // SIN INSERT de usuarios aquí
    }

    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }

    private static boolean tablaExiste(Connection conn, String nombreTabla) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, nombreTabla, null)) {
            return rs.next();
        }
    }

    private static void crearEstructura() throws Exception {
        if (tablasCreadas) return;

        ejecutarSQL("CREATE TABLE IF NOT EXISTS especialidades (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(80) NOT NULL," +
            "descripcion VARCHAR(200))");

        ejecutarSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombres VARCHAR(80) NOT NULL," +
            "apellidos VARCHAR(80) NOT NULL," +
            "documento VARCHAR(20) NOT NULL UNIQUE," +
            "email VARCHAR(100) NOT NULL UNIQUE," +
            "username VARCHAR(50) NOT NULL UNIQUE," +
            "password VARCHAR(255) NOT NULL," +
            "rol ENUM('MEDICO','RECEPCIONISTA','ENFERMERO') NOT NULL," +
            "especialidad VARCHAR(80)," +
            "lang_preferido VARCHAR(5) DEFAULT 'es'," +
            "activo TINYINT DEFAULT 1)");

        ejecutarSQL("CREATE TABLE IF NOT EXISTS pacientes (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombres VARCHAR(80) NOT NULL," +
            "apellidos VARCHAR(80) NOT NULL," +
            "documento VARCHAR(20) NOT NULL UNIQUE," +
            "fecha_nacimiento DATE NOT NULL," +
            "telefono VARCHAR(20)," +
            "email VARCHAR(100)," +
            "eps VARCHAR(80) NOT NULL," +
            "vereda_barrio VARCHAR(80))");

        ejecutarSQL("CREATE TABLE IF NOT EXISTS citas (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "id_paciente INT NOT NULL," +
            "id_medico INT NOT NULL," +
            "id_especialidad INT NOT NULL," +
            "fecha_cita DATE NOT NULL," +
            "hora_cita TIME NOT NULL," +
            "motivo VARCHAR(300)," +
            "estado ENUM('PROGRAMADA','CONFIRMADA','ATENDIDA','CANCELADA') DEFAULT 'PROGRAMADA'," +
            "observaciones VARCHAR(500)," +
            "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "id_registrado_por INT)");

        tablasCreadas = true;
    }
}
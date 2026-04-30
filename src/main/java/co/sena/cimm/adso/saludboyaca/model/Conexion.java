package co.sena.cimm.adso.saludboyaca.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    // Lee credenciales desde variables de entorno (para Docker)
    // Si no existen, usa valores locales por defecto
    private static final String URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://localhost:3306/saludboyaca?useSSL=false&serverTimezone=UTC";
    
    private static final String USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "root";
    
    private static final String PASS = System.getenv("DB_PASS") != null 
        ? System.getenv("DB_PASS") 
        : "1057585950";  // <-- Cambia esto por TU contraseña de MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error al cargar el driver de MySQL", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexion: " + ex.getMessage());
        }
    }
}
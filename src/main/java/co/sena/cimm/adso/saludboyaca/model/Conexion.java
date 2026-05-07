package co.sena.cimm.adso.saludboyaca.model;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexion {
    
    private static final HikariDataSource dataSource;
    
    static {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASS");
        
        // Fallback para desarrollo local
        if (dbUrl == null) {
            dbUrl = "jdbc:mysql://localhost:3306/saludboyaca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            dbUser = "root";
            dbPass = "1057585950";
        }
        
        // Agregar timeouts si no los tiene
        if (!dbUrl.contains("connectTimeout")) {
            dbUrl += "&connectTimeout=10000&socketTimeout=30000";
        }
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPass);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // <-- AGREGAR ESTO
        
        // Pool settings
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        // MySQL optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        System.out.println("=== INICIALIZANDO POOL ===");
        System.out.println("Driver: com.mysql.cj.jdbc.Driver");
        System.out.println("URL: " + dbUrl.replaceAll("://.*@", "://****@"));
        
        dataSource = new HikariDataSource(config);
        System.out.println("Pool creado exitosamente");
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar: " + ex.getMessage());
        }
    }
    
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
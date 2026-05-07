package co.sena.cimm.adso.saludboyaca.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {
    
    public int contarCitasHoy() {
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha_cita = CURDATE()";
        return ejecutarConteo(sql);
    }
    
    public int contarCitasPendientes() {
        String sql = "SELECT COUNT(*) FROM citas WHERE estado IN ('PROGRAMADA', 'CONFIRMADA')";
        return ejecutarConteo(sql);
    }
    
    public int contarCitasMes() {
        String sql = "SELECT COUNT(*) FROM citas WHERE MONTH(fecha_cita) = MONTH(CURDATE()) AND YEAR(fecha_cita) = YEAR(CURDATE())";
        return ejecutarConteo(sql);
    }
    
    public int contarPacientes() {
        String sql = "SELECT COUNT(*) FROM pacientes";
        return ejecutarConteo(sql);
    }
    
    public int contarMedicos() {
    String sql = "SELECT COUNT(*) FROM usuarios WHERE rol = 'MEDICO'";
    return ejecutarConteo(sql);
}
    
    public int contarEspecialidades() {
        String sql = "SELECT COUNT(*) FROM especialidades";
        return ejecutarConteo(sql);
    }
    
    private int ejecutarConteo(String sql) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error en DashboardDAO: " + e.getMessage());
        }
        return 0;
    }
}
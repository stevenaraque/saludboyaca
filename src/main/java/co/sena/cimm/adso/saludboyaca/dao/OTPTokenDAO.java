package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;

public class OTPTokenDAO {

    public boolean insertar(int idUsuario, String codigo, Timestamp expiraEn) {
        String sql = "INSERT INTO otp_tokens (id_usuario, codigo, expira_en) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setString(2, codigo);
            stmt.setTimestamp(3, expiraEn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en insertar OTP: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean validar(int idUsuario, String codigo) {
        String sql = "SELECT * FROM otp_tokens WHERE id_usuario = ? AND codigo = ? AND usado = 0 AND expira_en > NOW()";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setString(2, codigo);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.err.println("Error en validar OTP: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
    }

    public boolean marcarUsado(int idUsuario, String codigo) {
        String sql = "UPDATE otp_tokens SET usado = 1 WHERE id_usuario = ? AND codigo = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setString(2, codigo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en marcarUsado: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    private void cerrarRecursos(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) Conexion.closeConnection(conn);
        } catch (SQLException ex) {
            System.err.println("Error cerrando recursos: " + ex.getMessage());
        }
    }
}
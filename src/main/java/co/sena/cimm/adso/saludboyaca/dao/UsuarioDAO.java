package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en validarLogin: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorId: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios WHERE activo = 1 ORDER BY apellidos, nombres";
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return ejecutarUpdate(sql, u, false);
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombres=?, apellidos=?, documento=?, email=?, username=?, password=?, rol=?, especialidad=? WHERE id=?";
        return ejecutarUpdate(sql, u, true);
    }

    public boolean eliminar(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en eliminar: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setDocumento(rs.getString("documento"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setEspecialidad(rs.getString("especialidad"));
        u.setLangPreferido(rs.getString("lang_preferido"));
        u.setActivo(rs.getInt("activo"));
        return u;
    }

    private boolean ejecutarUpdate(String sql, Usuario u, boolean incluirId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getNombres());
            stmt.setString(2, u.getApellidos());
            stmt.setString(3, u.getDocumento());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getUsername());
            stmt.setString(6, u.getPassword());
            stmt.setString(7, u.getRol());
            stmt.setString(8, u.getEspecialidad());

            if (incluirId) {
                stmt.setInt(9, u.getId());
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en ejecutarUpdate: " + ex.getMessage());
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
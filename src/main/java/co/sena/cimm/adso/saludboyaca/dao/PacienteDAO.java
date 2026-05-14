package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    // ── CAMBIO 1: Campo para conexión externa (pruebas) ─────────
    private Connection conexionExterna; // null = usar Conexion.getConnection()

    // ── CAMBIO 2: Constructores ───────────────────────────────────
    public PacienteDAO() {
        this.conexionExterna = null;
    }

    // Constructor para pruebas de integración
    public PacienteDAO(Connection conexion) {
        this.conexionExterna = conexion;
    }

    // ── CAMBIO 3: Método auxiliar ───────────────────────────────────
    private Connection obtenerConexion() throws SQLException {
        return conexionExterna != null ? conexionExterna : Conexion.getConnection();
    }
    
    public boolean insertar(Paciente p) {
        String sql = "INSERT INTO pacientes (nombres, apellidos, documento, fecha_nacimiento, telefono, email, eps, vereda_barrio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getNombres());
            stmt.setString(2, p.getApellidos());
            stmt.setString(3, p.getDocumento());
            stmt.setDate(4, new java.sql.Date(p.getFechaNacimiento().getTime()));
            stmt.setString(5, p.getTelefono());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getEps());
            stmt.setString(8, p.getVeredaBarrio());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en insertar: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean actualizar(Paciente p) {
        String sql = "UPDATE pacientes SET nombres=?, apellidos=?, documento=?, fecha_nacimiento=?, telefono=?, email=?, eps=?, vereda_barrio=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getNombres());
            stmt.setString(2, p.getApellidos());
            stmt.setString(3, p.getDocumento());
            stmt.setDate(4, new java.sql.Date(p.getFechaNacimiento().getTime()));
            stmt.setString(5, p.getTelefono());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getEps());
            stmt.setString(8, p.getVeredaBarrio());
            stmt.setInt(9, p.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en actualizar: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
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

    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        return buscarUno(sql, id);
    }

    public Paciente buscarPorDocumento(String documento) {
        String sql = "SELECT * FROM pacientes WHERE documento = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paciente p = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            rs = stmt.executeQuery();

            if (rs.next()) {
                p = mapearPaciente(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorDocumento: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return p;
    }

    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM pacientes ORDER BY apellidos, nombres";
        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearPaciente(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    // ========== MÉTODOS PRIVADOS ==========

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setDocumento(rs.getString("documento"));
        p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setEps(rs.getString("eps"));
        p.setVeredaBarrio(rs.getString("vereda_barrio"));
        return p;
    }

    private Paciente buscarUno(String sql, int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paciente p = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                p = mapearPaciente(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarUno: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return p;
    }

    private boolean ejecutarUpdate(String sql, Paciente p, boolean incluirId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion(); // ← reemplazado
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getNombres());
            stmt.setString(2, p.getApellidos());
            stmt.setString(3, p.getDocumento());
            stmt.setDate(4, new java.sql.Date(p.getFechaNacimiento().getTime()));
            stmt.setString(5, p.getTelefono());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getEps());
            stmt.setString(8, p.getVeredaBarrio());

            if (incluirId) {
                stmt.setInt(9, p.getId());
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
            // Solo cerramos conexión si NO es externa (pruebas)
            if (conexionExterna == null && conn != null) {
                Conexion.closeConnection(conn);
            }
        } catch (SQLException ex) {
            System.err.println("Error cerrando recursos: " + ex.getMessage());
        }
    }
}
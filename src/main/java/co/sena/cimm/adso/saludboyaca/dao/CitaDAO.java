package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // ── CAMBIO 1: Campo para conexión externa (pruebas) ─────────
    private Connection conexionExterna;

    // ── CAMBIO 2: Constructores ───────────────────────────────────
    public CitaDAO() {
        this.conexionExterna = null;
    }

    public CitaDAO(Connection conexion) {
        this.conexionExterna = conexion;
    }

    // ── CAMBIO 3: Método auxiliar ───────────────────────────────────
    private Connection obtenerConexion() throws SQLException {
        return conexionExterna != null ? conexionExterna : Conexion.getConnection();
    }

    public boolean insertar(Cita c) {
        String sql = "INSERT INTO citas (id_paciente, id_medico, id_especialidad, fecha_cita, hora_cita, motivo, estado, id_registrado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getIdPaciente());
            stmt.setInt(2, c.getIdMedico());
            stmt.setInt(3, c.getIdEspecialidad());
            stmt.setDate(4, new java.sql.Date(c.getFechaCita().getTime()));
            stmt.setTime(5, c.getHoraCita());
            stmt.setString(6, c.getMotivo());
            stmt.setString(7, c.getEstado());
            stmt.setInt(8, c.getIdRegistradoPor());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en insertar: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET id_paciente=?, id_medico=?, id_especialidad=?, fecha_cita=?, hora_cita=?, motivo=?, estado=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, c.getIdPaciente());
            stmt.setInt(2, c.getIdMedico());
            stmt.setInt(3, c.getIdEspecialidad());
            stmt.setDate(4, new java.sql.Date(c.getFechaCita().getTime()));
            stmt.setTime(5, c.getHoraCita());
            stmt.setString(6, c.getMotivo());
            stmt.setString(7, c.getEstado());
            stmt.setInt(8, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en actualizar: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean cambiarEstado(int idCita, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idCita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error en cambiarEstado: " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, stmt, conn);
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
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

    public List<Cita> listarTodas() {
        String sql = "SELECT c.*, " +
                     "p.nombres as nom_paciente, p.apellidos as ape_paciente, p.documento as documento_paciente, " +
                     "u.nombres as nom_medico, u.apellidos as ape_medico, " +
                     "e.nombre as nom_especialidad " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.id_paciente = p.id " +
                     "JOIN usuarios u ON c.id_medico = u.id " +
                     "JOIN especialidades e ON c.id_especialidad = e.id " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita";
        return listarConJoin(sql);
    }

    public List<Cita> listarPorMedico(int medicoId) {
        String sql = "SELECT c.*, " +
                     "p.nombres as nom_paciente, p.apellidos as ape_paciente, p.documento as documento_paciente, " +
                     "u.nombres as nom_medico, u.apellidos as ape_medico, " +
                     "e.nombre as nom_especialidad " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.id_paciente = p.id " +
                     "JOIN usuarios u ON c.id_medico = u.id " +
                     "JOIN especialidades e ON c.id_especialidad = e.id " +
                     "WHERE c.id_medico = ? " +
                     "ORDER BY c.fecha_cita DESC, c.hora_cita";
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, medicoId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearCitaConJoin(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorMedico: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    public List<Cita> listarPorPaciente(String documento) {
        String sql = "SELECT c.*, " +
                     "p.nombres as nom_paciente, p.apellidos as ape_paciente, p.documento as documento_paciente, " +
                     "u.nombres as nom_medico, u.apellidos as ape_medico, " +
                     "e.nombre as nom_especialidad " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.id_paciente = p.id " +
                     "JOIN usuarios u ON c.id_medico = u.id " +
                     "JOIN especialidades e ON c.id_especialidad = e.id " +
                     "WHERE p.documento = ? " +
                     "ORDER BY c.fecha_cita DESC";
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearCitaConJoin(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorPaciente: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    public Cita buscarPorId(int id) {
        String sql = "SELECT c.*, " +
                     "p.nombres as nom_paciente, p.apellidos as ape_paciente, p.documento as documento_paciente, " +
                     "u.nombres as nom_medico, u.apellidos as ape_medico, " +
                     "e.nombre as nom_especialidad " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.id_paciente = p.id " +
                     "JOIN usuarios u ON c.id_medico = u.id " +
                     "JOIN especialidades e ON c.id_especialidad = e.id " +
                     "WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cita c = null;
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                c = mapearCitaConJoin(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorId: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return c;
    }

    private List<Cita> listarConJoin(String sql) {
        List<Cita> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearCitaConJoin(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarConJoin: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    private Cita mapearCitaConJoin(ResultSet rs) throws SQLException {
        Cita c = new Cita();
        c.setId(rs.getInt("id"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setIdMedico(rs.getInt("id_medico"));
        c.setIdEspecialidad(rs.getInt("id_especialidad"));
        c.setFechaCita(rs.getDate("fecha_cita"));
        c.setHoraCita(rs.getTime("hora_cita"));
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        c.setIdRegistradoPor(rs.getInt("id_registrado_por"));
        c.setNombrePaciente(rs.getString("nom_paciente") + " " + rs.getString("ape_paciente"));
        c.setDocumentoPaciente(rs.getString("documento_paciente"));
        c.setNombreMedico(rs.getString("nom_medico") + " " + rs.getString("ape_medico"));
        c.setNombreEspecialidad(rs.getString("nom_especialidad"));
        return c;
    }

    private void cerrarRecursos(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conexionExterna == null && conn != null) {
                Conexion.closeConnection(conn);
            }
        } catch (SQLException ex) {
            System.err.println("Error cerrando recursos: " + ex.getMessage());
        }
    }
}
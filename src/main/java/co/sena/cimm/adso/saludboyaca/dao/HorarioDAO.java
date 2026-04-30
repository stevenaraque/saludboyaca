package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Horario;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {

    public List<Horario> listarPorMedico(int idMedico) {
        String sql = "SELECT * FROM horarios WHERE id_medico = ? ORDER BY dia_semana, hora_inicio";
        List<Horario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMedico);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearHorario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorMedico: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    public List<Horario> horasDisponibles(int idMedico, java.sql.Date fecha) {
        // Este método es más complejo, lo completamos después
        // Por ahora devuelve los horarios del médico
        return listarPorMedico(idMedico);
    }

    private Horario mapearHorario(ResultSet rs) throws SQLException {
        Horario h = new Horario();
        h.setId(rs.getInt("id"));
        h.setIdMedico(rs.getInt("id_medico"));
        h.setDiaSemana(rs.getInt("dia_semana"));
        h.setHoraInicio(rs.getTime("hora_inicio"));
        h.setHoraFin(rs.getTime("hora_fin"));
        h.setMaxCitas(rs.getInt("max_citas"));
        return h;
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
package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Especialidad;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    public List<Especialidad> listarTodas() {
        String sql = "SELECT * FROM especialidades ORDER BY nombre";
        List<Especialidad> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearEspecialidad(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodas: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return lista;
    }

    public Especialidad buscarPorId(int id) {
        String sql = "SELECT * FROM especialidades WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Especialidad e = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                e = mapearEspecialidad(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorId: " + ex.getMessage());
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        return e;
    }

    private Especialidad mapearEspecialidad(ResultSet rs) throws SQLException {
        Especialidad e = new Especialidad();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setDescripcion(rs.getString("descripcion"));
        return e;
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
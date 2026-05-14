package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.CitaResumenDTO;
import co.sena.cimm.adso.saludboyaca.model.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    // Citas de hoy para todos los roles (RECEPCIONISTA, ENFERMERO)
    public List<CitaResumenDTO> getCitasHoy() {
        String sql = """
                SELECT c.id,
                       CONCAT(p.nombres, ' ', p.apellidos) AS pacienteNombre,
                       e.nombre                            AS especialidadNombre,
                       c.fecha_cita                        AS fechaCita,
                       c.hora_cita                         AS horaCita,
                       c.estado
                FROM citas c
                JOIN pacientes p      ON c.id_paciente    = p.id
                JOIN especialidades e ON c.id_especialidad = e.id
                WHERE c.fecha_cita = CURDATE()
                ORDER BY c.hora_cita ASC
                LIMIT 10
                """;
        return ejecutarConsultaCitas(sql, -1);
    }

    // Citas de hoy filtradas solo por el médico autenticado
    public List<CitaResumenDTO> getCitasHoyPorMedico(int medicoId) {
        String sql = """
                SELECT c.id,
                       CONCAT(p.nombres, ' ', p.apellidos) AS pacienteNombre,
                       e.nombre                            AS especialidadNombre,
                       c.fecha_cita                        AS fechaCita,
                       c.hora_cita                         AS horaCita,
                       c.estado
                FROM citas c
                JOIN pacientes p      ON c.id_paciente    = p.id
                JOIN especialidades e ON c.id_especialidad = e.id
                WHERE c.fecha_cita = CURDATE()
                  AND c.id_medico   = ?
                ORDER BY c.hora_cita ASC
                LIMIT 10
                """;
        return ejecutarConsultaCitas(sql, medicoId);
    }

    // Método reutilizable: si medicoId == -1 no aplica filtro por médico
    private List<CitaResumenDTO> ejecutarConsultaCitas(String sql, int medicoId) {
        List<CitaResumenDTO> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (medicoId != -1) {
                ps.setInt(1, medicoId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CitaResumenDTO dto = new CitaResumenDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setPacienteNombre(rs.getString("pacienteNombre"));
                    dto.setEspecialidadNombre(rs.getString("especialidadNombre"));
                    dto.setFechaCita(rs.getString("fechaCita"));
                    dto.setHoraCita(rs.getString("horaCita"));
                    dto.setEstado(rs.getString("estado"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en DashboardDAO.getCitas: " + e.getMessage());
        }
        return lista;
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
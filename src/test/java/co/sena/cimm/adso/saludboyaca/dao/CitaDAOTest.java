package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CitaDAOTest {

    private CitaDAO citaDAO;
    private PacienteDAO pacienteDAO;
    private Connection conn;

    @BeforeAll
    static void init() throws Exception {
        ConexionTestHelper.inicializar();
    }

    @BeforeEach
    void setUp() throws Exception {
        ConexionTestHelper.limpiarTablas();
        conn = ConexionTestHelper.getConnection();
        citaDAO = new CitaDAO(conn);
        pacienteDAO = new PacienteDAO(conn);
        ConexionTestHelper.ejecutarSQL(
            "INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad) " +
            "VALUES ('Gregory','House','MEDICO-BASE-001','house@test.com','drhouse','pass123','MEDICO','Medicina General')"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    private int idPacientePorDocumento(String documento) throws Exception {
        try (Connection c = ConexionTestHelper.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(
                 "SELECT id FROM pacientes WHERE documento = '" + documento + "'")) {
            rs.next();
            return rs.getInt("id");
        }
    }

    private int idMedicoBase() throws Exception {
        try (Connection c = ConexionTestHelper.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(
                 "SELECT id FROM usuarios WHERE username = 'drhouse'")) {
            rs.next();
            return rs.getInt("id");
        }
    }

    @Test
    @Order(1)
    @DisplayName("insertar() debe guardar cita en H2")
    void insertar_debeGuardarCita() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Ana Lucia");
        p.setApellidos("Gutierrez Paez");
        p.setDocumento("1052345681");
        p.setFechaNacimiento(java.sql.Date.valueOf("1985-03-15"));
        p.setEps("Sanitas");
        pacienteDAO.insertar(p);

        int idPaciente = idPacientePorDocumento("1052345681");
        int idMedico = idMedicoBase();

        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(1);
        c.setFechaCita(java.sql.Date.valueOf("2026-05-20"));
        c.setHoraCita(Time.valueOf("08:00:00"));
        c.setMotivo("Control tension arterial");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);

        assertTrue(citaDAO.insertar(c));
    }

    @Test
    @Order(2)
    @DisplayName("listarTodas() debe retornar citas con JOIN de paciente, medico y especialidad")
    void listarTodas_debeRetornarCitasConJoin() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Luis Fernando");
        p.setApellidos("Rojas Casas");
        p.setDocumento("1052345682");
        p.setFechaNacimiento(java.sql.Date.valueOf("1990-07-22"));
        p.setEps("Compensar");
        pacienteDAO.insertar(p);

        int idPaciente = idPacientePorDocumento("1052345682");
        int idMedico = idMedicoBase();

        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(1);
        c.setFechaCita(java.sql.Date.valueOf("2026-05-21"));
        c.setHoraCita(Time.valueOf("09:00:00"));
        c.setMotivo("Dolor de cabeza");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);
        citaDAO.insertar(c);

        List<Cita> lista = citaDAO.listarTodas();

        assertEquals(1, lista.size());
        assertEquals("Luis Fernando Rojas Casas", lista.get(0).getNombrePaciente());
        assertEquals("Medicina General", lista.get(0).getNombreEspecialidad());
        assertNotNull(lista.get(0).getNombreMedico());
    }

    @Test
    @Order(3)
    @DisplayName("cambiarEstado() debe actualizar estado de la cita")
    void cambiarEstado_debeActualizar() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Diana Marcela");
        p.setApellidos("Arias Bernal");
        p.setDocumento("1052345683");
        p.setFechaNacimiento(java.sql.Date.valueOf("1978-11-08"));
        p.setEps("Nueva EPS");
        pacienteDAO.insertar(p);

        int idPaciente = idPacientePorDocumento("1052345683");
        int idMedico = idMedicoBase();

        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(1);
        c.setFechaCita(java.sql.Date.valueOf("2026-05-22"));
        c.setHoraCita(Time.valueOf("10:00:00"));
        c.setMotivo("Consulta general");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);
        citaDAO.insertar(c);

        List<Cita> citas = citaDAO.listarTodas();
        int idCita = citas.get(0).getId();

        boolean resultado = citaDAO.cambiarEstado(idCita, "CONFIRMADA");

        assertTrue(resultado);
        Cita actualizada = citaDAO.buscarPorId(idCita);
        assertEquals("CONFIRMADA", actualizada.getEstado());
    }

    @Test
    @Order(4)
    @DisplayName("eliminar() debe reducir conteo de citas")
    void eliminar_debeReducirConteo() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Pedro Jose");
        p.setApellidos("Martinez Vega");
        p.setDocumento("1052345684");
        p.setFechaNacimiento(java.sql.Date.valueOf("1995-01-30"));
        p.setEps("Sura");
        pacienteDAO.insertar(p);

        int idPaciente = idPacientePorDocumento("1052345684");
        int idMedico = idMedicoBase();

        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(1);
        c.setFechaCita(java.sql.Date.valueOf("2026-05-23"));
        c.setHoraCita(Time.valueOf("11:00:00"));
        c.setMotivo("Revision");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);
        citaDAO.insertar(c);

        List<Cita> antes = citaDAO.listarTodas();
        int idEliminar = antes.get(0).getId();

        citaDAO.eliminar(idEliminar);

        List<Cita> despues = citaDAO.listarTodas();
        assertEquals(antes.size() - 1, despues.size());
    }

    @Test
    @Order(5)
    @DisplayName("listarPorPaciente() debe encontrar citas por documento")
    void listarPorPaciente_debeEncontrar() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Claudia Milena");
        p.setApellidos("Beltran Torres");
        p.setDocumento("1052345685");
        p.setFechaNacimiento(java.sql.Date.valueOf("1982-06-10"));
        p.setEps("Sanitas");
        pacienteDAO.insertar(p);

        int idPaciente = idPacientePorDocumento("1052345685");
        int idMedico = idMedicoBase();

        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(2);
        c.setFechaCita(java.sql.Date.valueOf("2026-05-24"));
        c.setHoraCita(Time.valueOf("14:00:00"));
        c.setMotivo("Limpieza dental");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);
        citaDAO.insertar(c);

        List<Cita> lista = citaDAO.listarPorPaciente("1052345685");

        assertEquals(1, lista.size());
        assertEquals("Claudia Milena Beltran Torres", lista.get(0).getNombrePaciente());
        assertEquals("Odontologia", lista.get(0).getNombreEspecialidad());
    }

    // ==================== BONUS A ====================
    @Test
    @Order(6)
    @DisplayName("BONUS A: Flujo completo paciente + medico + cita + verificar JOIN")
    void flujoCompleto_pacienteMedicoCita_debeRetornarEnListarTodas() throws Exception {
        // ARRANGE: Insertar paciente
        Paciente p = new Paciente();
        p.setNombres("Steven Alejandro");
        p.setApellidos("Araque Castro");
        p.setDocumento("1052345699");
        p.setFechaNacimiento(java.sql.Date.valueOf("1995-08-15"));
        p.setEps("Sanitas");
        assertTrue(pacienteDAO.insertar(p), "Paciente debe insertarse");

        // ARRANGE: Insertar medico (usuario)
        ConexionTestHelper.ejecutarSQL(
            "INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad) " +
            "VALUES ('Dra. Maria','Fernandez','MEDICO-002','maria@test.com','drafernandez','pass456','MEDICO','Pediatria')"
        );

        int idPaciente = idPacientePorDocumento("1052345699");
        int idMedico;
        try (Connection c = ConexionTestHelper.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id FROM usuarios WHERE username = 'drafernandez'")) {
            rs.next();
            idMedico = rs.getInt("id");
        }

        // ARRANGE: Insertar cita
        Cita c = new Cita();
        c.setIdPaciente(idPaciente);
        c.setIdMedico(idMedico);
        c.setIdEspecialidad(3); // Pediatria
        c.setFechaCita(java.sql.Date.valueOf("2026-06-15"));
        c.setHoraCita(Time.valueOf("10:30:00"));
        c.setMotivo("Control pediatrico");
        c.setEstado("PROGRAMADA");
        c.setIdRegistradoPor(idMedico);
        assertTrue(citaDAO.insertar(c), "Cita debe insertarse");

        // ACT: Listar todas las citas
        List<Cita> lista = citaDAO.listarTodas();

        // ASSERT: Verificar flujo completo
        assertEquals(1, lista.size(), "Debe existir exactamente 1 cita");
        Cita obtenida = lista.get(0);
        assertEquals("Steven Alejandro Araque Castro", obtenida.getNombrePaciente());
        assertEquals("Dra. Maria Fernandez", obtenida.getNombreMedico());
        assertEquals("Pediatria", obtenida.getNombreEspecialidad());
        assertEquals("PROGRAMADA", obtenida.getEstado());
    }

    // ==================== BONUS B ====================
    @Test
    @Order(7)
    @DisplayName("BONUS B: listarTodas() con 50 citas debe responder en menos de 500ms")
    void listarTodas_con50Citas_debeResponderEnMenosDe500ms() throws Exception {
        // ARRANGE: Insertar 50 pacientes y 50 citas
        int idMedico = idMedicoBase();

        for (int i = 1; i <= 50; i++) {
            Paciente p = new Paciente();
            p.setNombres("Paciente" + i);
            p.setApellidos("Apellido" + i);
            p.setDocumento("DOC" + String.format("%03d", i));
            p.setFechaNacimiento(java.sql.Date.valueOf("1990-01-01"));
            p.setEps("EPS" + i);
            pacienteDAO.insertar(p);

            int idPaciente;
            try (Connection c = ConexionTestHelper.getConnection();
                 Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT id FROM pacientes WHERE documento = 'DOC" + String.format("%03d", i) + "'")) {
                rs.next();
                idPaciente = rs.getInt("id");
            }

            Cita c = new Cita();
            c.setIdPaciente(idPaciente);
            c.setIdMedico(idMedico);
            c.setIdEspecialidad(1);
            c.setFechaCita(java.sql.Date.valueOf("2026-07-" + String.format("%02d", (i % 30) + 1)));
            c.setHoraCita(Time.valueOf("08:00:00"));
            c.setMotivo("Motivo " + i);
            c.setEstado("PROGRAMADA");
            c.setIdRegistradoPor(idMedico);
            citaDAO.insertar(c);
        }

        // ACT & ASSERT: Medir tiempo de respuesta
        assertTimeout(java.time.Duration.ofMillis(500), () -> {
            List<Cita> lista = citaDAO.listarTodas();
            assertEquals(50, lista.size(), "Deben existir exactamente 50 citas");
        }, "listarTodas() con 50 registros debe responder en menos de 500ms");
    }
}
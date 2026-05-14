package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacienteDAOTest {

    private PacienteDAO dao;
    private Connection conn;

    @BeforeAll
    static void init() throws Exception {
        ConexionTestHelper.inicializar();
    }

    @BeforeEach
    void setUp() throws Exception {
        ConexionTestHelper.limpiarTablas();
        conn = ConexionTestHelper.getConnection();
        dao = new PacienteDAO(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    @Test
    @Order(1)
    @DisplayName("insertar() debe guardar paciente en H2")
    void insertar_debeGuardarPaciente() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Ana Lucia");
        p.setApellidos("Gutierrez Paez");
        p.setDocumento("1052345681");
        p.setFechaNacimiento(java.sql.Date.valueOf("1985-03-15"));
        p.setTelefono("3123456789");
        p.setEmail("agutierrez@gmail.com");
        p.setEps("Sanitas");
        p.setVeredaBarrio("Centro");
        assertTrue(dao.insertar(p));
    }

    @Test
    @Order(2)
    @DisplayName("listarTodos() debe retornar pacientes insertados")
    void listarTodos_debeRetornarPacientes() throws Exception {
        Paciente p1 = new Paciente();
        p1.setNombres("Luis Fernando");
        p1.setApellidos("Rojas Casas");
        p1.setDocumento("1052345682");
        p1.setFechaNacimiento(java.sql.Date.valueOf("1990-07-22"));
        p1.setEps("Compensar");
        dao.insertar(p1);

        Paciente p2 = new Paciente();
        p2.setNombres("Diana Marcela");
        p2.setApellidos("Arias Bernal");
        p2.setDocumento("1052345683");
        p2.setFechaNacimiento(java.sql.Date.valueOf("1978-11-08"));
        p2.setEps("Nueva EPS");
        dao.insertar(p2);

        assertEquals(2, dao.listarTodos().size());
    }

    @Test
    @Order(3)
    @DisplayName("insertar() con documento duplicado debe fallar")
    void insertar_documentoDuplicado_debeFallar() throws Exception {
        Paciente p1 = new Paciente();
        p1.setNombres("Carlos");
        p1.setApellidos("Pedraza");
        p1.setDocumento("1052345678");
        p1.setFechaNacimiento(java.sql.Date.valueOf("1985-03-15"));
        p1.setEps("Sanitas");
        dao.insertar(p1);

        Paciente p2 = new Paciente();
        p2.setNombres("Otro");
        p2.setApellidos("Nombre");
        p2.setDocumento("1052345678");
        p2.setFechaNacimiento(java.sql.Date.valueOf("1990-01-01"));
        p2.setEps("Compensar");
        assertFalse(dao.insertar(p2));
    }

    @Test
    @Order(4)
    @DisplayName("buscarPorDocumento() debe encontrar paciente")
    void buscarPorDocumento_debeEncontrar() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Sandra Liliana");
        p.setApellidos("Mora Cifuentes");
        p.setDocumento("1052345687");
        p.setFechaNacimiento(java.sql.Date.valueOf("1993-12-03"));
        p.setEps("Nueva EPS");
        dao.insertar(p);

        Paciente encontrado = dao.buscarPorDocumento("1052345687");
        assertNotNull(encontrado);
        assertEquals("Sandra Liliana", encontrado.getNombres());
    }

    @Test
    @Order(5)
    @DisplayName("eliminar() debe reducir conteo")
    void eliminar_debeReducirConteo() throws Exception {
        Paciente p = new Paciente();
        p.setNombres("Juan Sebastian");
        p.setApellidos("Herrera Acosta");
        p.setDocumento("1052345692");
        p.setFechaNacimiento(java.sql.Date.valueOf("2000-02-14"));
        p.setEps("Compensar");
        dao.insertar(p);

        List<Paciente> antes = dao.listarTodos();
        int idEliminar = antes.get(0).getId();
        dao.eliminar(idEliminar);

        assertEquals(antes.size() - 1, dao.listarTodos().size());
    }
}
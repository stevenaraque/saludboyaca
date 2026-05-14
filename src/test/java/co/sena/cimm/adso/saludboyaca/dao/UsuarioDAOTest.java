package co.sena.cimm.adso.saludboyaca.dao;

import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioDAOTest {

    private UsuarioDAO dao;
    private Connection conn;

    @BeforeAll
    static void init() throws Exception {
        ConexionTestHelper.inicializar();
    }

    @BeforeEach
    void setUp() throws Exception {
        ConexionTestHelper.limpiarTablas();
        conn = ConexionTestHelper.getConnection();
        dao = new UsuarioDAO(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    @Test
    @Order(1)
    @DisplayName("insertar() debe guardar usuario en H2")
    void insertar_debeGuardarUsuario() {
        Usuario u = new Usuario();
        u.setNombres("Admin");
        u.setApellidos("Sistema");
        u.setDocumento("1000000000");
        u.setEmail("admin@test.com");
        u.setUsername("admin");
        u.setPassword("admin123");
        u.setRol("MEDICO");
        u.setEspecialidad("Administración");
        assertTrue(dao.insertar(u));
    }

    @Test
    @Order(2)
    @DisplayName("validarLogin() debe retornar usuario con credenciales correctas")
    void validarLogin_conCredencialesCorrectas() {
        Usuario u = new Usuario();
        u.setNombres("María");
        u.setApellidos("López");
        u.setDocumento("52345678");
        u.setEmail("maria@test.com");
        u.setUsername("mlopez");
        u.setPassword("enf123");
        u.setRol("ENFERMERO");
        dao.insertar(u);

        Usuario logueado = dao.validarLogin("mlopez", "enf123");
        assertNotNull(logueado);
        assertEquals("María López", logueado.getNombreCompleto());
        assertEquals("ENFERMERO", logueado.getRol());
    }

    @Test
    @Order(3)
    @DisplayName("validarLogin() con password incorrecto debe retornar null")
    void validarLogin_conPasswordIncorrecto() {
        Usuario u = new Usuario();
        u.setNombres("Carlos");
        u.setApellidos("Martínez");
        u.setDocumento("80123456");
        u.setEmail("carlos@test.com");
        u.setUsername("cmartinez");
        u.setPassword("correcta");
        u.setRol("MEDICO");
        dao.insertar(u);

        assertNull(dao.validarLogin("cmartinez", "incorrecta"));
    }

    @Test
    @Order(4)
    @DisplayName("listarTodos() debe retornar solo usuarios activos")
    void listarTodos_soloActivos() {
        Usuario u1 = new Usuario();
        u1.setNombres("Ana");
        u1.setApellidos("Rodríguez");
        u1.setDocumento("80234567");
        u1.setEmail("ana@test.com");
        u1.setUsername("arodriguez");
        u1.setPassword("pass1");
        u1.setRol("MEDICO");
        dao.insertar(u1);

        Usuario u2 = new Usuario();
        u2.setNombres("Luis");
        u2.setApellidos("Gómez");
        u2.setDocumento("80345678");
        u2.setEmail("luis@test.com");
        u2.setUsername("lgomez");
        u2.setPassword("pass2");
        u2.setRol("MEDICO");
        dao.insertar(u2);

        assertEquals(2, dao.listarTodos().size());
    }

    @Test
    @Order(5)
    @DisplayName("eliminar() debe desactivar usuario (no borrarlo)")
    void eliminar_debeDesactivarUsuario() {
        Usuario u = new Usuario();
        u.setNombres("Pedro");
        u.setApellidos("Sánchez");
        u.setDocumento("80456789");
        u.setEmail("pedro@test.com");
        u.setUsername("psanchez");
        u.setPassword("pass3");
        u.setRol("RECEPCIONISTA");
        dao.insertar(u);

        List<Usuario> antes = dao.listarTodos();
        assertEquals(1, antes.size());

        assertTrue(dao.eliminar(antes.get(0).getId()));
        assertEquals(0, dao.listarTodos().size(),
            "Usuario desactivado no debe aparecer en listarTodos");
    }
}
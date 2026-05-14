package co.sena.cimm.adso.saludboyaca.dto;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario medico;
    private Usuario enfermero;
    private Usuario recepcionista;

    @BeforeEach
    void setUp() {
        medico = new Usuario();
        medico.setId(1);
        medico.setNombres("Carlos");
        medico.setApellidos("Martinez");
        medico.setDocumento("80123456");
        medico.setEmail("cmartinez@test.com");
        medico.setUsername("cmartinez");
        medico.setPassword("pass123");
        medico.setRol("MEDICO");
        medico.setEspecialidad("Medicina General");
        medico.setActivo(1);

        enfermero = new Usuario();
        enfermero.setId(2);
        enfermero.setNombres("Maria");
        enfermero.setApellidos("Lopez");
        enfermero.setDocumento("52345678");
        enfermero.setEmail("maria@test.com");
        enfermero.setUsername("mlopez");
        enfermero.setPassword("enf123");
        enfermero.setRol("ENFERMERO");
        enfermero.setActivo(1);

        recepcionista = new Usuario();
        recepcionista.setId(3);
        recepcionista.setNombres("Luis");
        recepcionista.setApellidos("Gomez");
        recepcionista.setDocumento("80345678");
        recepcionista.setEmail("luis@test.com");
        recepcionista.setUsername("lgomez");
        recepcionista.setPassword("pass2");
        recepcionista.setRol("RECEPCIONISTA");
        recepcionista.setActivo(1);
    }

    @Test
    @DisplayName("esMedico() debe retornar true para usuario con rol MEDICO")
    void esMedico_conRolMedico_debeRetornarTrue() {
        assertTrue(medico.esMedico());
    }

    @Test
    @DisplayName("esMedico() debe retornar false para usuario con rol ENFERMERO")
    void esMedico_conRolEnfermero_debeRetornarFalse() {
        assertFalse(enfermero.esMedico());
    }

    @Test
    @DisplayName("esEnfermero() debe retornar true para usuario con rol ENFERMERO")
    void esEnfermero_conRolEnfermero_debeRetornarTrue() {
        assertTrue(enfermero.esEnfermero());
    }

    @Test
    @DisplayName("esRecepcionista() debe retornar true para usuario con rol RECEPCIONISTA")
    void esRecepcionista_conRolRecepcionista_debeRetornarTrue() {
        assertTrue(recepcionista.esRecepcionista());
    }

    @Test
    @DisplayName("esMedico() debe ser insensible a mayusculas")
    void esMedico_debeSerInsensibleAMayusculas() {
        medico.setRol("medico");
        assertTrue(medico.esMedico());
    }

    @Test
    @DisplayName("getNombreCompleto() para el medico del sistema")
    void getNombreCompleto_paraMedico() {
        assertEquals("Carlos Martinez", medico.getNombreCompleto());
    }

    @Test
    @DisplayName("estaActivo() debe retornar true para usuario activo")
    void estaActivo_usuarioActivo_debeRetornarTrue() {
        assertTrue(medico.estaActivo());
    }

    @Test
    @DisplayName("estaActivo() debe retornar false para usuario inactivo")
    void estaActivo_usuarioInactivo_debeRetornarFalse() {
        medico.setActivo(0);
        assertFalse(medico.estaActivo());
    }

    @Test
    @DisplayName("Usuario con rol null NO debe ser medico ni enfermero")
    void usuarioSinRol_noEsMedicoNiEnfermero() {
        medico.setRol(null);
        assertFalse(medico.esMedico());
        assertFalse(medico.esEnfermero());
    }
}
package co.sena.cimm.adso.saludboyaca.dto;

import org.junit.jupiter.api.*;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

public class PacienteTest {

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setNombres("Ana Lucia");
        paciente.setApellidos("Gutierrez Paez");
        paciente.setDocumento("1052345681");
        paciente.setFechaNacimiento(Date.valueOf("1985-03-15"));
        paciente.setTelefono("3123456789");
        paciente.setEmail("agutierrez@gmail.com");
        paciente.setEps("Sanitas");
        paciente.setVeredaBarrio("Centro");
    }

    @Test
    @DisplayName("getNombreCompleto() debe unir nombres y apellidos con espacio")
    void getNombreCompleto_debeUnirNombresYApellidos() {
        String resultado = paciente.getNombreCompleto();
        assertEquals("Ana Lucia Gutierrez Paez", resultado);
    }

    @Test
    @DisplayName("getNombreCompleto() con nombres vacios debe retornar espacio y apellidos")
    void getNombreCompleto_conNombresVacios() {
        paciente.setNombres("");
        String resultado = paciente.getNombreCompleto();
        assertTrue(resultado.contains("Gutierrez Paez"));
    }

    @Test
    @DisplayName("Constructor debe inicializar todos los campos correctamente")
    void constructor_debeInicializarTodosLosCampos() {
        assertAll("Campos de Paciente",
            () -> assertEquals(1, paciente.getId()),
            () -> assertEquals("Ana Lucia", paciente.getNombres()),
            () -> assertEquals("Gutierrez Paez", paciente.getApellidos()),
            () -> assertEquals("1052345681", paciente.getDocumento()),
            () -> assertEquals("Sanitas", paciente.getEps())
        );
    }

    @Test
    @DisplayName("setDocumento() debe actualizar el documento correctamente")
    void setDocumento_debeActualizarDocumento() {
        paciente.setDocumento("9999999999");
        assertEquals("9999999999", paciente.getDocumento());
    }

    @Test
    @DisplayName("Paciente vacio (constructor sin parametros) debe tener id=0")
    void constructorVacio_debeCrearPacienteConIdCero() {
        Paciente vacio = new Paciente();
        assertEquals(0, vacio.getId());
    }

    @Test
    @DisplayName("Nombre completo de paciente real de Boyaca")
    void pacienteBoyaca_nombreCompleto() {
        Paciente boyaca = new Paciente();
        boyaca.setNombres("Maria Eugenia");
        boyaca.setApellidos("Suarez Cely");
        boyaca.setDocumento("1052345679");
        boyaca.setFechaNacimiento(Date.valueOf("1992-07-24"));
        boyaca.setEps("Compensar");
        assertEquals("Maria Eugenia Suarez Cely", boyaca.getNombreCompleto());
    }

    @Test
    @DisplayName("Nombre completo no debe ser null")
    void nombreCompleto_noDebeSerNull() {
        assertNotNull(paciente.getNombreCompleto());
    }

    @Test
    @DisplayName("Nombre completo no debe estar vacio")
    void nombreCompleto_noDebeEstarVacio() {
        assertFalse(paciente.getNombreCompleto().isBlank());
    }
}
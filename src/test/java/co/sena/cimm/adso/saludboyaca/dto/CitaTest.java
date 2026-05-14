package co.sena.cimm.adso.saludboyaca.dto;

import org.junit.jupiter.api.*;
import java.sql.Time;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

class CitaTest {

    private Cita citaProgramada;
    private Cita citaConfirmada;
    private Cita citaCancelada;

    @BeforeEach
    void setUp() {
        citaProgramada = new Cita();
        citaProgramada.setId(1);
        citaProgramada.setIdPaciente(1);
        citaProgramada.setIdMedico(1);
        citaProgramada.setIdEspecialidad(1);
        citaProgramada.setFechaCita(Date.valueOf("2026-05-20"));
        citaProgramada.setHoraCita(Time.valueOf("08:00:00"));
        citaProgramada.setMotivo("Control tension arterial");
        citaProgramada.setEstado("PROGRAMADA");
        citaProgramada.setNombrePaciente("Ana Lucia Gutierrez Paez");
        citaProgramada.setNombreMedico("Dr. Carlos Martinez");
        citaProgramada.setNombreEspecialidad("Medicina General");

        citaConfirmada = new Cita();
        citaConfirmada.setId(2);
        citaConfirmada.setEstado("CONFIRMADA");
        citaConfirmada.setNombrePaciente("Luis Fernando Rojas");
        citaConfirmada.setNombreEspecialidad("Odontologia");

        citaCancelada = new Cita();
        citaCancelada.setId(3);
        citaCancelada.setEstado("CANCELADA");
    }

    @Test
    @DisplayName("estaProgramada() debe retornar true para estado PROGRAMADA")
    void estaProgramada_conEstadoProgramada_debeRetornarTrue() {
        assertTrue(citaProgramada.estaProgramada());
    }

    @Test
    @DisplayName("estaProgramada() debe retornar false para estado CONFIRMADA")
    void estaProgramada_conEstadoConfirmada_debeRetornarFalse() {
        assertFalse(citaConfirmada.estaProgramada());
    }

    @Test
    @DisplayName("estaConfirmada() debe retornar true para estado CONFIRMADA")
    void estaConfirmada_conEstadoConfirmada_debeRetornarTrue() {
        assertTrue(citaConfirmada.estaConfirmada());
    }

    @Test
    @DisplayName("estaCancelada() debe retornar true para estado CANCELADA")
    void estaCancelada_conEstadoCancelada_debeRetornarTrue() {
        assertTrue(citaCancelada.estaCancelada());
    }

    @Test
    @DisplayName("tieneObservaciones() debe retornar false cuando observaciones es null")
    void tieneObservaciones_conNull_debeRetornarFalse() {
        assertFalse(citaProgramada.tieneObservaciones());
    }

    @Test
    @DisplayName("tieneObservaciones() debe retornar true cuando hay texto")
    void tieneObservaciones_conTexto_debeRetornarTrue() {
        citaProgramada.setObservaciones("Traer examenes previos");
        assertTrue(citaProgramada.tieneObservaciones());
    }

    @Test
    @DisplayName("tieneObservaciones() debe retornar false cuando observaciones esta vacia")
    void tieneObservaciones_conVacio_debeRetornarFalse() {
        citaProgramada.setObservaciones("   ");
        assertFalse(citaProgramada.tieneObservaciones());
    }

    @Test
    @DisplayName("getResumenCita() debe retornar formato correcto")
    void getResumenCita_debeRetornarFormatoCorrecto() {
        String esperado = "Ana Lucia Gutierrez Paez - Medicina General (2026-05-20)";
        assertEquals(esperado, citaProgramada.getResumenCita());
    }

    @Test
    @DisplayName("Constructor debe asignar todos los campos correctamente")
    void constructor_debeAsignarTodosLosCampos() {
        assertAll("Campos de Cita",
            () -> assertEquals(1, citaProgramada.getId()),
            () -> assertEquals(1, citaProgramada.getIdPaciente()),
            () -> assertEquals(1, citaProgramada.getIdMedico()),
            () -> assertEquals(1, citaProgramada.getIdEspecialidad()),
            () -> assertEquals("PROGRAMADA", citaProgramada.getEstado())
        );
    }

    @Test
    @DisplayName("Cita con estado null no debe lanzar excepcion en estaProgramada()")
    void estaProgramada_conEstadoNull_debeRetornarFalse() {
        Cita sinEstado = new Cita();
        sinEstado.setEstado(null);
        assertDoesNotThrow(() -> sinEstado.estaProgramada());
        assertFalse(sinEstado.estaProgramada());
    }
}
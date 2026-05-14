package co.sena.cimm.adso.saludboyaca.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidadorDocumentoTest {

    private ValidadorDocumento validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorDocumento();
    }

    @ParameterizedTest(name = "Documento valido: [{0}]")
    @ValueSource(strings = {
        "1000000000",
        "1052345678",
        "52345678",
        "123456",
        "105345",
        "9999999999"
    })
    @DisplayName("Documentos validos deben retornar true")
    void documentoValido_debeRetornarTrue(String doc) {
        assertTrue(validador.esValido(doc), "Se esperaba que '" + doc + "' fuera valido");
    }

    @ParameterizedTest(name = "Documento invalido: [{0}]")
    @ValueSource(strings = {
        "12345",
        "12345678901",
        "CC-123456",
        "105 345",
        ""
    })
    @DisplayName("Documentos invalidos deben retornar false")
    void documentoInvalido_debeRetornarFalse(String doc) {
        assertFalse(validador.esValido(doc));
    }

    @Test
    @DisplayName("null debe retornar false sin lanzar NullPointerException")
    void documentoNull_debeRetornarFalse() {
        assertFalse(validador.esValido(null));
    }

    @ParameterizedTest(name = "doc={0}, esperado={1}")
    @CsvSource({
        "1052345678, true",
        "1000000000, true",
        "52345678, false",
        "2052345678, false",
        "123456, false"
    })
    @DisplayName("esTarjetaIdentidad() con varios documentos")
    void esTarjetaIdentidad_variosDocumentos(String doc, boolean esperado) {
        assertEquals(esperado, validador.esTarjetaIdentidad(doc));
    }

    @Test
    @DisplayName("Documento con exactamente 6 digitos (minimo) debe ser valido")
    void documento_6digitos_debeSerValido() {
        assertTrue(validador.esValido("123456"));
    }

    @Test
    @DisplayName("Documento con exactamente 10 digitos (maximo) debe ser valido")
    void documento_10digitos_debeSerValido() {
        assertTrue(validador.esValido("1052345678"));
    }

    @Test
    @DisplayName("Documento con 5 digitos (debajo del minimo) debe ser invalido")
    void documento_5digitos_debeSerInvalido() {
        assertFalse(validador.esValido("12345"));
    }

    @Test
    @DisplayName("Documento con 11 digitos (encima del maximo) debe ser invalido")
    void documento_11digitos_debeSerInvalido() {
        assertFalse(validador.esValido("10523456780"));
    }
}
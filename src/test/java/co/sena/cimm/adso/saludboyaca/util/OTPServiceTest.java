package co.sena.cimm.adso.saludboyaca.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OTPServiceTest {

    @Test
    @DisplayName("generarOTP() debe retornar string de exactamente 6 digitos")
    void generarOTP_debeRetornar6Digitos() {
        String otp = OTPService.generarOTP();
        assertEquals(6, otp.length());
    }

    @Test
    @DisplayName("generarOTP() debe retornar solo caracteres numericos")
    void generarOTP_debeRetornarSoloDigitos() {
        String otp = OTPService.generarOTP();
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    @DisplayName("generarOTP() dos llamadas consecutivas deben retornar valores diferentes")
    void generarOTP_debeSerAleatorio() {
        String otp1 = OTPService.generarOTP();
        String otp2 = OTPService.generarOTP();
        assertNotEquals(otp1, otp2);
    }

    @Test
    @DisplayName("esValido() debe retornar true cuando coinciden y no han expirado")
    void esValido_coincidenYNoExpirado_debeRetornarTrue() {
        String otp = "123456";
        long ahora = System.currentTimeMillis();
        assertTrue(OTPService.esValido(otp, otp, ahora));
    }

    @Test
    @DisplayName("esValido() debe retornar false cuando los OTP no coinciden")
    void esValido_noCoinciden_debeRetornarFalse() {
        long ahora = System.currentTimeMillis();
        assertFalse(OTPService.esValido("123456", "654321", ahora));
    }

    @Test
    @DisplayName("esValido() debe retornar false cuando el OTP expiro")
    void esValido_expirado_debeRetornarFalse() {
        String otp = "123456";
        long hace6Minutos = System.currentTimeMillis() - (6 * 60 * 1000);
        assertFalse(OTPService.esValido(otp, otp, hace6Minutos));
    }

    @Test
    @DisplayName("esValido() debe retornar false cuando ingresado es null")
    void esValido_ingresadoNull_debeRetornarFalse() {
        long ahora = System.currentTimeMillis();
        assertFalse(OTPService.esValido(null, "123456", ahora));
    }

    @Test
    @DisplayName("esValido() debe retornar false cuando guardado es null")
    void esValido_guardadoNull_debeRetornarFalse() {
        long ahora = System.currentTimeMillis();
        assertFalse(OTPService.esValido("123456", null, ahora));
    }

    @Test
    @DisplayName("enmascararEmail() debe ocultar despues del tercer caracter")
    void enmascararEmail_emailNormal_debeEnmascarar() {
        String resultado = OTPService.enmascararEmail("stevenalejandro@gmail.com");
        assertEquals("ste***@gmail.com", resultado);
    }

    @Test
    @DisplayName("enmascararEmail() debe retornar *** para email null")
    void enmascararEmail_null_debeRetornarTresAsteriscos() {
        assertEquals("***", OTPService.enmascararEmail(null));
    }

    @Test
    @DisplayName("enmascararEmail() debe retornar *** para email sin arroba")
    void enmascararEmail_sinArroba_debeRetornarTresAsteriscos() {
        assertEquals("***", OTPService.enmascararEmail("correoinvalido"));
    }

    @Test
    @DisplayName("enmascararEmail() debe manejar email corto")
    void enmascararEmail_emailCorto_debeEnmascarar() {
        String resultado = OTPService.enmascararEmail("ab@gmail.com");
        assertEquals("ab***@gmail.com", resultado);
    }
}
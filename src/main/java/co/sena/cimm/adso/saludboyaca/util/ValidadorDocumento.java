package co.sena.cimm.adso.saludboyaca.util;

public class ValidadorDocumento {
    
    private static final int MIN_DIGITOS = 6;
    private static final int MAX_DIGITOS = 10;
    
    public boolean esValido(String documento) {
        if (documento == null || documento.isBlank()) {
            return false;
        }
        String limpio = documento.trim();
        if (!limpio.matches("\\d+")) {
            return false;
        }
        return limpio.length() >= MIN_DIGITOS && limpio.length() <= MAX_DIGITOS;
    }
    
    public boolean esTarjetaIdentidad(String documento) {
        if (!esValido(documento)) {
            return false;
        }
        return documento.trim().length() == 10 && documento.trim().startsWith("1");
    }
}
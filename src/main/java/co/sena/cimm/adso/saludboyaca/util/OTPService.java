package co.sena.cimm.adso.saludboyaca.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;

public class OTPService {

    private static final int OTP_LONGITUD = 6;
    private static final long OTP_EXPIRA_MS = 5 * 60 * 1000;

    private static final String RESEND_API_KEY = System.getenv("RESEND_API_KEY") != null
            ? System.getenv("RESEND_API_KEY")
            : "re_tu_nueva_api_key_aqui";

    public static String generarOTP() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LONGITUD);
        for (int i = 0; i < OTP_LONGITUD; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean esValido(String ingresado, String guardado, long timestamp) {
        if (ingresado == null || guardado == null) {
            return false;
        }
        long ahora = Instant.now().toEpochMilli();
        boolean noExpirado = (ahora - timestamp) <= OTP_EXPIRA_MS;
        boolean coincide = ingresado.trim().equals(guardado);
        return noExpirado && coincide;
    }

    public static void enviarOTP(String destinatario, String codigoOTP, String asunto, String cuerpo)
            throws Exception {

        // ============================================
        // SIEMPRE MOSTRAR EN CONSOLA (para desarrollo)
        // ============================================
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🔐 CÓDIGO OTP SALUDBOYACÁ                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Destinatario: " + String.format("%-44s", destinatario) + " ║");
        System.out.println("║  Código OTP:   " + String.format("%-44s", codigoOTP) + " ║");
        System.out.println("║  Asunto:       " + String.format("%-44s", asunto) + " ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        // ============================================
        // INTENTAR ENVIAR POR CORREO (Resend)
        // ============================================
        try {
            System.out.println("[RESEND] Intentando enviar OTP a: " + destinatario);

            URL url = new URL("https://api.resend.com/emails");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + RESEND_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            String remitente = "onboarding@resend.dev";

// Siempre enviar a tu Gmail (único correo permitido con onboarding@resend.dev)
            String destinatarioFinal = "stevenalejandroaraquecastro@gmail.com";
            System.out.println("[RESEND] Enviando OTP a: " + destinatarioFinal + " (correo real: " + destinatario + ")");

            String jsonBody = "{"
                    + "\"from\":\"" + escaparJson(remitente) + "\","
                    + "\"to\":[\"" + escaparJson(destinatarioFinal) + "\"],"
                    + "\"subject\":\"" + escaparJson(asunto) + "\","
                    + "\"text\":\"" + escaparJson(cuerpo) + "\""
                    + "}";

            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } finally {
                if (os != null) {
                    os.close();
                }
            }

            int responseCode = conn.getResponseCode();
            System.out.println("[RESEND] HTTP " + responseCode);

            if (responseCode == 200 || responseCode == 201) {
                String response = leerStream(conn.getInputStream());
                System.out.println("[RESEND] ✅ Enviado exitosamente: " + response);
            } else {
                String error = leerStream(conn.getErrorStream());
                System.err.println("[RESEND] ❌ Error HTTP " + responseCode + ": " + error);
                // NO lanzamos excepción, solo logueamos el error
                System.out.println("[RESEND] ⚠️ El correo falló, pero el código está disponible en consola arriba ↑");
            }

            conn.disconnect();

        } catch (UnknownHostException e) {
            System.err.println("[RESEND] ❌ Sin conexión a internet o DNS no resuelve api.resend.com");
            System.out.println("[RESEND] ⚠️ Modo offline activado - usa el código mostrado en consola ↑");
        } catch (Exception e) {
            System.err.println("[RESEND] ❌ Error enviando correo: " + e.getMessage());
            System.out.println("[RESEND] ⚠️ El código OTP sigue siendo válido - revisa consola arriba ↑");
        }
    }

    // Metodo helper para leer InputStream compatible con Java 8
    private static String leerStream(InputStream stream) throws Exception {
        if (stream == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    // Metodo helper para escapar caracteres JSON
    public static String escaparJson(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static Timestamp calcularExpiracion() {
        return new Timestamp(System.currentTimeMillis() + OTP_EXPIRA_MS);
    }

    public static String enmascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] partes = email.split("@");
        String local = partes[0];
        String dominio = partes[1];
        if (local.length() <= 3) {
            return local + "***@" + dominio;
        }
        return local.substring(0, 3) + "***@" + dominio;
    }
}

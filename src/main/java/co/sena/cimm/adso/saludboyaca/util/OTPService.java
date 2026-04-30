package co.sena.cimm.adso.saludboyaca.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class OTPService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    
    // Lee credenciales desde variables de entorno
    private static final String EMAIL_REMIT = System.getenv("EMAIL_REMIT") != null 
        ? System.getenv("EMAIL_REMIT") 
        : "tucorreo@gmail.com";  // <-- Cambia esto o usa variable de entorno
    
    private static final String EMAIL_PASS = System.getenv("EMAIL_PASS") != null 
        ? System.getenv("EMAIL_PASS") 
        : "xxxx xxxx xxxx xxxx";  // <-- App Password de Gmail (16 caracteres)
    
    private static final int OTP_LONGITUD = 6;
    private static final long OTP_EXPIRA_MS = 5 * 60 * 1000; // 5 minutos

    public static String generarOTP() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LONGITUD);
        for (int i = 0; i < OTP_LONGITUD; i++) {
            sb.append(rnd.nextInt(10)); // dígito 0-9
        }
        return sb.toString();
    }

    public static boolean esValido(String ingresado, String guardado, long timestamp) {
        if (ingresado == null || guardado == null) return false;
        long ahora = Instant.now().toEpochMilli();
        boolean noExpirado = (ahora - timestamp) <= OTP_EXPIRA_MS;
        boolean coincide = ingresado.trim().equals(guardado);
        return noExpirado && coincide;
    }

    public static void enviarOTP(String destinatario, String codigoOTP, String asunto, String cuerpo)
            throws MessagingException, UnsupportedEncodingException {
        
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMIT, EMAIL_PASS);
            }
        });

        Message mensaje = new MimeMessage(mailSession);
        mensaje.setFrom(new InternetAddress(EMAIL_REMIT, "SaludBoyaca - Centro de Salud"));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        Transport.send(mensaje);
    }

    public static Timestamp calcularExpiracion() {
        return new Timestamp(System.currentTimeMillis() + OTP_EXPIRA_MS);
    }

    public static String enmascararEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        String[] partes = email.split("@");
        String local = partes[0];
        String dominio = partes[1];
        if (local.length() <= 3) return local + "***@" + dominio;
        return local.substring(0, 3) + "***@" + dominio;
    }
}
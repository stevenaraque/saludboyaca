package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAO;
import co.sena.cimm.adso.saludboyaca.dao.UsuarioDAO;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import co.sena.cimm.adso.saludboyaca.util.OTPService;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.concurrent.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private OTPTokenDAO otpTokenDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        otpTokenDAO = new OTPTokenDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado"))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long inicio = System.currentTimeMillis();
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            System.out.println("=== LOGIN POST === username: " + username);

            HttpSession session = request.getSession();
            String lang = (String) session.getAttribute("lang");
            if (lang == null) {
                lang = "es";
            }

            ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

            System.out.println("Validando credenciales...");
            Usuario usuario = usuarioDAO.validarLogin(username, password);
            System.out.println("Validacion: " + (usuario != null ? "OK" : "FALLIDO"));

            if (usuario != null) {
                String otp = OTPService.generarOTP();
                long timestamp = System.currentTimeMillis();
                Timestamp expiraEn = OTPService.calcularExpiracion();

                System.out.println("Guardando OTP en BD...");
                otpTokenDAO.insertar(usuario.getId(), otp, expiraEn);

                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuarioNombre", usuario.getNombreCompleto());
                session.setAttribute("usuarioRol", usuario.getRol());
                session.setAttribute("otpCodigo", otp);
                session.setAttribute("otpTimestamp", timestamp);
                session.setAttribute("otpEmail", usuario.getEmail());
                session.setAttribute("otpVerificado", false);

                String asunto = rb.getString("otp.email.asunto");
                String cuerpo = MessageFormat.format(rb.getString("otp.email.cuerpo"), otp);

                // === INTENTAR ENVIAR CORREO (NO BLOQUEANTE, NO CRITICO) ===
                final String emailDestino = usuario.getEmail();
                final String otpFinal = otp;
                final String asuntoFinal = asunto;
                final String cuerpoFinal = cuerpo;

                new Thread(() -> {
                    try {
                        System.out.println("[EMAIL] Intentando enviar a: " + emailDestino);
                        OTPService.enviarOTP(emailDestino, otpFinal, asuntoFinal, cuerpoFinal);
                        System.out.println("[EMAIL] ✅ Enviado correctamente");
                    } catch (Exception ex) {
                        System.err.println("[EMAIL] ⚠️ No se pudo enviar: " + ex.getClass().getSimpleName());
                        // No es critico, el OTP esta en pantalla
                    }
                }).start();

                // Guardar OTP en sesion para mostrar en pantalla
                session.setAttribute("otpMostrarEnPantalla", otp);

                long duracion = System.currentTimeMillis() - inicio;
                System.out.println("Login en " + duracion + "ms. OTP: " + otp);
                
                response.sendRedirect(request.getContextPath() + "/otp");
                return;

            } else {
                request.setAttribute("error", rb.getString("login.error.credenciales"));
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }

        } catch (Exception e) {
            System.err.println("ERROR CRITICO: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error del servidor: " + e.getClass().getSimpleName());
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
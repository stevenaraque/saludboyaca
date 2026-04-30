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
        
        // Si ya está logueado y verificado, ir al dashboard
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
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");
        if (lang == null) lang = "es";
        
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));
        
        // Validar credenciales
        Usuario usuario = usuarioDAO.validarLogin(username, password);
        
        if (usuario != null) {
            // Credenciales válidas → generar OTP
            String otp = OTPService.generarOTP();
            long timestamp = System.currentTimeMillis();
            Timestamp expiraEn = OTPService.calcularExpiracion();
            
            // Guardar OTP en base de datos
            otpTokenDAO.insertar(usuario.getId(), otp, expiraEn);
            
            // Guardar datos en sesión (sin otpVerificado aún)
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNombre", usuario.getNombreCompleto());
            session.setAttribute("usuarioRol", usuario.getRol());
            session.setAttribute("otpCodigo", otp);
            session.setAttribute("otpTimestamp", timestamp);
            session.setAttribute("otpEmail", usuario.getEmail());
            session.setAttribute("otpVerificado", false);
            
            // Preparar y enviar correo
            String asunto = rb.getString("otp.email.asunto");
            String cuerpo = MessageFormat.format(rb.getString("otp.email.cuerpo"), otp);
            
            try {
                OTPService.enviarOTP(usuario.getEmail(), otp, asunto, cuerpo);
            } catch (Exception ex) {
                System.err.println("Error enviando OTP: " + ex.getMessage());
                // En desarrollo, mostrar el OTP en consola
                System.out.println("===== OTP PARA PRUEBAS: " + otp + " =====");
            }
            
            // Redirigir a verificación OTP
            response.sendRedirect(request.getContextPath() + "/otp");
            
        } else {
            // Credenciales incorrectas
            request.setAttribute("error", rb.getString("login.error.credenciales"));
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAO;
import co.sena.cimm.adso.saludboyaca.util.OTPService;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "OTPServlet", urlPatterns = {"/otp"})
public class OTPServlet extends HttpServlet {

    private OTPTokenDAO otpTokenDAO;

    @Override
    public void init() throws ServletException {
        otpTokenDAO = new OTPTokenDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Si no hay sesion o no hay OTP pendiente, ir al login
        if (session == null || session.getAttribute("otpCodigo") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Mostrar email enmascarado
        String email = (String) session.getAttribute("otpEmail");
        request.setAttribute("emailMasked", OTPService.enmascararEmail(email));
        
        request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // Detectar si es reenvio o verificacion
        String action = request.getParameter("action");
        
        if ("reenviar".equals(action)) {
            doReenviar(request, response);
            return;
        }
        
        // Si no hay action, es verificacion normal
        doVerificar(request, response);
    }

    // ============================================
    // VERIFICACION NORMAL
    // ============================================
    private void doVerificar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String codigoIngresado = request.getParameter("otpCodigo");
        String codigoGuardado = (String) session.getAttribute("otpCodigo");
        Long timestampObj = (Long) session.getAttribute("otpTimestamp");
        Integer usuarioIdObj = (Integer) session.getAttribute("usuarioId");
        
        // Validar que existan los datos en sesion
        if (codigoGuardado == null || timestampObj == null || usuarioIdObj == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        long timestamp = timestampObj.longValue();
        int usuarioId = usuarioIdObj.intValue();
        
        String lang = (String) session.getAttribute("lang");
        if (lang == null) lang = "es";
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));
        
        // Validar OTP
        if (OTPService.esValido(codigoIngresado, codigoGuardado, timestamp)) {
            
            // Verificar tambien en base de datos
            if (otpTokenDAO.validar(usuarioId, codigoIngresado)) {
                
                // OTP correcto -> marcar como usado y verificado
                otpTokenDAO.marcarUsado(usuarioId, codigoIngresado);
                session.setAttribute("otpVerificado", true);
                
                // Limpiar datos temporales de OTP
                session.removeAttribute("otpCodigo");
                session.removeAttribute("otpTimestamp");
                
                // Ir al dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
        }
        
        // OTP incorrecto o expirado
        request.setAttribute("error", rb.getString("otp.error"));
        String email = (String) session.getAttribute("otpEmail");
        request.setAttribute("emailMasked", OTPService.enmascararEmail(email));
        request.getRequestDispatcher("/views/otp_verificacion.jsp").forward(request, response);
    }

    // ============================================
    // REENVIAR OTP
    // ============================================
    private void doReenviar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // JSON manual (sin dependencias externas)
        StringBuilder json = new StringBuilder();
        
        if (session == null) {
            json.append("{\"success\":false,\"message\":\"Sesion expirada\"}");
            out.print(json.toString());
            return;
        }
        
        String email = (String) session.getAttribute("otpEmail");
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null) nombreUsuario = "Usuario";
        
        if (email == null || usuarioId == null) {
            json.append("{\"success\":false,\"message\":\"Datos de sesion invalidos\"}");
            out.print(json.toString());
            return;
        }
        
        try {
    // Generar nuevo OTP
    String nuevoOTP = OTPService.generarOTP();
    Timestamp expiracion = OTPService.calcularExpiracion();
    long nuevoTimestamp = System.currentTimeMillis();
    
    System.out.println("[OTP REENVIAR] ============================================");
    System.out.println("[OTP REENVIAR] Generando nuevo OTP para usuario: " + usuarioId);
    System.out.println("[OTP REENVIAR] Email: " + email);
    System.out.println("[OTP REENVIAR] ============================================");
    
    // Invalidar OTP anterior en BD
    otpTokenDAO.invalidarAnteriores(usuarioId);
    System.out.println("[OTP REENVIAR] ✅ OTP anteriores invalidados");
    
    // Guardar nuevo OTP en BD
    boolean guardado = otpTokenDAO.insertar(usuarioId, nuevoOTP, expiracion);
    
    if (!guardado) {
        json.append("{\"success\":false,\"message\":\"Error al guardar el nuevo codigo en base de datos\"}");
        out.print(json.toString());
        return;
    }
    System.out.println("[OTP REENVIAR] ✅ Nuevo OTP guardado en BD");
    
    // Enviar por correo (y siempre por consola)
    String asunto = "SaludBoyaca - Nuevo codigo de verificacion";
    String cuerpo = "Hola " + nombreUsuario + ",\n\n"
            + "Tu nuevo codigo de verificacion es: " + nuevoOTP + "\n\n"
            + "Este codigo expira en 5 minutos.\n\n"
            + "Si no solicitaste este codigo, ignora este mensaje.\n\n"
            + "SaludBoyaca";
    
    OTPService.enviarOTP(email, nuevoOTP, asunto, cuerpo);
    
    // Actualizar sesion
    session.setAttribute("otpCodigo", nuevoOTP);
    session.setAttribute("otpTimestamp", nuevoTimestamp);
    
    System.out.println("[OTP REENVIAR] ✅ Sesion actualizada con nuevo OTP");
    System.out.println("[OTP REENVIAR] ============================================");
    
    json.append("{\"success\":true,\"message\":\"Codigo reenviado. Revisa tu correo o la consola del servidor.\"}");
    
} catch (Exception e) {
    System.err.println("[OTP REENVIAR] ❌ ERROR: " + e.getMessage());
    e.printStackTrace();
    json.append("{\"success\":false,\"message\":\"Error al reenviar: ").append(escaparJson(e.getMessage())).append("\"}");
}
        
        out.print(json.toString());
    }
    
    // Helper para escapar caracteres especiales en JSON
    private String escaparJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "")
                    .replace("\t", "\\t");
    }
}
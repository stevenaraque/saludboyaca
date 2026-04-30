package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.OTPTokenDAO;
import co.sena.cimm.adso.saludboyaca.util.OTPService;
import java.io.IOException;
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
        
        // Si no hay sesión o no hay OTP pendiente, ir al login
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
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String codigoIngresado = request.getParameter("otpCodigo");
        String codigoGuardado = (String) session.getAttribute("otpCodigo");
        long timestamp = (Long) session.getAttribute("otpTimestamp");
        int usuarioId = (Integer) session.getAttribute("usuarioId");
        
        String lang = (String) session.getAttribute("lang");
        if (lang == null) lang = "es";
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));
        
        // Validar OTP
        if (OTPService.esValido(codigoIngresado, codigoGuardado, timestamp)) {
            
            // Verificar también en base de datos
            if (otpTokenDAO.validar(usuarioId, codigoIngresado)) {
                
                // OTP correcto → marcar como usado y verificado
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
}
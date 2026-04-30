package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ConsultaCitaServlet", urlPatterns = {"/consulta"})
public class ConsultaCitaServlet extends HttpServlet {
    
    private CitaDAO citaDAO;
    
    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("menu", "consulta");
        request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String documento = request.getParameter("documento");
        String captchaInput = request.getParameter("captcha");
        
        HttpSession session = request.getSession();
        String captchaSession = (String) session.getAttribute("captchaCode");
        
        String lang = (String) session.getAttribute("lang");
        if (lang == null) lang = "es";
        
        // Validar CAPTCHA
        if (captchaSession == null || !captchaSession.equalsIgnoreCase(captchaInput)) {
            request.setAttribute("error", "consulta.captcha.error");
            request.setAttribute("documento", documento);
            request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
            return;
        }
        
        // Buscar citas del paciente
        List<Cita> citas = citaDAO.listarPorPaciente(documento);
        
        request.setAttribute("citas", citas);
        request.setAttribute("documento", documento);
        request.setAttribute("resultado", true);
        
        request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
    }
}
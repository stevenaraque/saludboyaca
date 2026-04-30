package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.util.PDFGenerator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DescargarComprobanteServlet", urlPatterns = {"/comprobante"})
public class DescargarComprobanteServlet extends HttpServlet {
    
    private CitaDAO citaDAO;
    
    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCita = Integer.parseInt(request.getParameter("id"));
            Cita cita = citaDAO.buscarPorId(idCita);
            
            if (cita == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada");
                return;
            }
            
            String lang = (String) request.getSession().getAttribute("lang");
            if (lang == null) lang = "es";
            
            // Generar PDF
            byte[] pdfBytes = PDFGenerator.generarComprobanteCita(cita, lang);
            
            // Configurar respuesta
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=comprobante_cita_" + idCita + ".pdf");
            response.setContentLength(pdfBytes.length);
            
            // Enviar PDF
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando PDF");
        }
    }
}
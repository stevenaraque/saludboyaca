package co.sena.cimm.adso.saludboyaca.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "IdiomaServlet", urlPatterns = {"/idioma"})
public class IdiomaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String lang = request.getParameter("lang");
        HttpSession session = request.getSession(false);
        
        PrintWriter out = response.getWriter();
        
        // Validar que haya sesion
        if (session == null) {
            out.print("{\"ok\":false,\"error\":\"no_session\"}");
            return;
        }
        
        // Validar idioma soportado
        if (lang == null || (!lang.equals("es") && !lang.equals("en") && !lang.equals("it"))) {
            out.print("{\"ok\":false,\"error\":\"idioma_no_soportado\"}");
            return;
        }
        
        // Guardar en sesion (igual que tu LocaleFilter)
        session.setAttribute("locale", new Locale(lang));
        session.setAttribute("lang", lang);
        
        // Responder OK
        out.print("{\"ok\":true,\"lang\":\"" + lang + "\"}");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // El cambio de idioma por GET lo dejamos para el LocaleFilter (links normales)
        // Este servlet solo responde POST para AJAX
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST");
    }
}
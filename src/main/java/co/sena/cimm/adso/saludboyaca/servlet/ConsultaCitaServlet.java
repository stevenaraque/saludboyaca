package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
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
        
        // Mantener el idioma si viene por parámetro
        String lang = request.getParameter("lang");
        if (lang != null) {
            HttpSession session = request.getSession();
            session.setAttribute("lang", lang);
            session.setAttribute("locale", lang);
        }
        
        request.setAttribute("menu", "consulta");
        request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String documento = request.getParameter("documento");
        String captchaInput = request.getParameter("captcha");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        
        HttpSession session = request.getSession();
        String captchaSession = (String) session.getAttribute("captchaCode");
        
        // Validar Google reCAPTCHA v2
        boolean recaptchaValido = verificarRecaptcha(gRecaptchaResponse);
        if (!recaptchaValido) {
            request.setAttribute("error", "recaptcha.invalido");
            request.setAttribute("documento", documento);
            request.getRequestDispatcher("/views/consulta_cita.jsp").forward(request, response);
            return;
        }
        
        // Validar CAPTCHA propio
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
    
    // Método para verificar reCAPTCHA con Google (SIN librerías externas)
    private boolean verificarRecaptcha(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }
        
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String secretKey = "6LdlRNMsAAAAAK84WJUMQXb1TPR5ugpdw72oF4bk";
        
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            
            String postParams = "secret=" + secretKey + "&response=" + gRecaptchaResponse;
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder respuesta = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                respuesta.append(inputLine);
            }
            in.close();
            
            // Parseo manual SIN librería JSON
            String json = respuesta.toString();
            return json.contains("\"success\": true");
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
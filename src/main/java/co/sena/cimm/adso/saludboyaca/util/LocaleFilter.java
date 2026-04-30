package co.sena.cimm.adso.saludboyaca.util;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        String lang = httpRequest.getParameter("lang");
        
        if (lang != null && !lang.isEmpty()) {
            // Validar idiomas soportados: es, en, it
            if (lang.equals("es") || lang.equals("en") || lang.equals("it")) {
                Locale locale = new Locale(lang);
                if (session != null) {
                    session.setAttribute("locale", locale);
                    session.setAttribute("lang", lang);
                }
            }
        }
        
        // Si no hay idioma en sesión, poner español por defecto
        if (session != null && session.getAttribute("locale") == null) {
            session.setAttribute("locale", new Locale("es"));
            session.setAttribute("lang", "es");
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
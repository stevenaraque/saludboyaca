package co.sena.cimm.adso.saludboyaca.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {
    "/dashboard",
    "/pacientes/*",
    "/citas/*",
    "/horarios/*",
    "/usuarios/*"
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean isLoggedIn = (session != null 
                && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado")));
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
                // URLs públicas (no requieren autenticación)
        boolean isPublicURL = requestURI.endsWith("/login") 
                || requestURI.endsWith("/otp")
                || requestURI.endsWith("/consulta")
                || requestURI.contains("/captcha")      // <-- AGREGAR ESTA LÍNEA
                || requestURI.contains("/resources/");
        
        if (isLoggedIn || isPublicURL) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
    }
}
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
    "/usuarios/*",
    "/reportes/*",
    "/idioma"
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
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // URLs públicas (no requieren autenticación)
        boolean isPublicURL = requestURI.endsWith("/login") 
        || requestURI.endsWith("/otp")
        || requestURI.endsWith("/consulta")
        || requestURI.contains("/captcha")
        || requestURI.contains("/resources/")
        || requestURI.endsWith("/idioma");
        
        if (isPublicURL) {
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar sesión y OTP verificado
        boolean isLoggedIn = (session != null 
                && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado")));
        
        if (!isLoggedIn) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Obtener rol del usuario
        String rol = (String) session.getAttribute("usuarioRol");
        
        // Control de acceso por rol
        if (requestURI.contains("/usuarios")) {
            // Solo RECEPCIONISTA puede gestionar usuarios
            if (!"RECEPCIONISTA".equals(rol)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }
        
        if (requestURI.contains("/horarios")) {
            // Solo MEDICO y RECEPCIONISTA pueden ver horarios
            if (!"MEDICO".equals(rol) && !"RECEPCIONISTA".equals(rol)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }
        
        if (requestURI.contains("/citas")) {
            String accion = httpRequest.getParameter("accion"); // <-- CORREGIDO: era "action"
            // Solo RECEPCIONISTA puede crear/editar/eliminar citas
            if (accion != null && (accion.equals("nuevo") || accion.equals("guardar") 
                    || accion.equals("editar") || accion.equals("eliminar"))) {
                if (!"RECEPCIONISTA".equals(rol)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                    return;
                }
            }
            // MEDICO solo puede ver citas y marcar como atendida
            if ("MEDICO".equals(rol) && accion != null 
                    && !accion.equals("listar") && !accion.equals("atender") && !accion.equals("detalle")) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }
        
        if (requestURI.contains("/pacientes")) {
            String accion = httpRequest.getParameter("accion"); // <-- CORREGIDO: era "action"
            // Solo RECEPCIONISTA puede crear/editar/eliminar pacientes
            if (accion != null && (accion.equals("nuevo") || accion.equals("guardar") 
                    || accion.equals("editar") || accion.equals("eliminar"))) {
                if (!"RECEPCIONISTA".equals(rol)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                    return;
                }
            }
        }
        
        // Si pasa todas las validaciones, continuar
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
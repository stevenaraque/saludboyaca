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

        // URLs públicas
        boolean isPublicURL = requestURI.endsWith("/login")
                || requestURI.endsWith("/otp")
                || requestURI.endsWith("/consulta")
                || requestURI.contains("/captcha")
                || requestURI.contains("/resources/")
                || requestURI.endsWith("/idioma")
                || requestURI.endsWith("/comprobante");

        if (isPublicURL) {
            chain.doFilter(request, response);
            return;
        }

        // Verificar sesión activa y OTP verificado
        boolean isLoggedIn = (session != null
                && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado")));

        if (!isLoggedIn) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        String rol = (String) session.getAttribute("usuarioRol");

        // Solo RECEPCIONISTA puede gestionar usuarios
        if (requestURI.contains("/usuarios")) {
            if (!"RECEPCIONISTA".equals(rol)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }

        // Horarios: solo MEDICO y RECEPCIONISTA
        if (requestURI.contains("/horarios")) {
            if (!"MEDICO".equals(rol) && !"RECEPCIONISTA".equals(rol)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }

        // Citas: RECEPCIONISTA y MEDICO pueden crear/editar; ENFERMERO solo consulta
        if (requestURI.contains("/citas")) {
            String accion = httpRequest.getParameter("accion");
            if (accion != null) {
                boolean esAccionEscritura = accion.equals("nuevo")
                        || accion.equals("guardar")
                        || accion.equals("editar")
                        || accion.equals("eliminar");

                // ENFERMERO no puede hacer acciones de escritura
                if (esAccionEscritura && "ENFERMERO".equals(rol)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                    return;
                }

                // Solo RECEPCIONISTA puede eliminar citas
                if (accion.equals("eliminar") && !"RECEPCIONISTA".equals(rol)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                    return;
                }
            }
        }

        // Pacientes: solo RECEPCIONISTA puede crear/editar/eliminar
        if (requestURI.contains("/pacientes")) {
            String accion = httpRequest.getParameter("accion");
            if (accion != null && (accion.equals("nuevo") || accion.equals("guardar")
                    || accion.equals("editar") || accion.equals("eliminar"))) {
                if (!"RECEPCIONISTA".equals(rol)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
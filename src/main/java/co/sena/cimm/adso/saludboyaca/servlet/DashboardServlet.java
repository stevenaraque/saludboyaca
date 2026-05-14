package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.DashboardDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private DashboardDAO dashboardDAO;

    @Override
    public void init() throws ServletException {
        dashboardDAO = new DashboardDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String rol = (String) session.getAttribute("usuarioRol");

        // Estadísticas generales (visibles para todos los roles)
        request.setAttribute("citasHoy", dashboardDAO.contarCitasHoy());
        request.setAttribute("citasPendientes", dashboardDAO.contarCitasPendientes());
        request.setAttribute("citasMes", dashboardDAO.contarCitasMes());
        request.setAttribute("totalPacientes", dashboardDAO.contarPacientes());
        request.setAttribute("totalMedicos", dashboardDAO.contarMedicos());
        request.setAttribute("totalEspecialidades", dashboardDAO.contarEspecialidades());

        // Citas recientes: si es MEDICO solo ve las suyas, los demás ven todas las de hoy
        if ("MEDICO".equals(rol)) {
            // Asegúrate que al hacer login guardas el id como Integer en sesión
            // session.setAttribute("usuarioId", usuario.getId());
            Integer medicoId = (Integer) session.getAttribute("usuarioId");
            if (medicoId != null) {
                request.setAttribute("citasRecientes", dashboardDAO.getCitasHoyPorMedico(medicoId));
            } else {
                // Fallback por si el id no está en sesión
                request.setAttribute("citasRecientes", dashboardDAO.getCitasHoy());
            }
        } else {
            request.setAttribute("citasRecientes", dashboardDAO.getCitasHoy());
        }

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
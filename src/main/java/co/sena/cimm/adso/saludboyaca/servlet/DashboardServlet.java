package co.sena.cimm.adso.saludboyaca.controller;

import co.sena.cimm.adso.saludboyaca.model.DashboardDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        
        // Verificar autenticación
        if (request.getSession().getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Cargar estadísticas
        request.setAttribute("citasHoy", dashboardDAO.contarCitasHoy());
        request.setAttribute("citasPendientes", dashboardDAO.contarCitasPendientes());
        request.setAttribute("citasMes", dashboardDAO.contarCitasMes());
        request.setAttribute("totalPacientes", dashboardDAO.contarPacientes());
        request.setAttribute("totalMedicos", dashboardDAO.contarMedicos());
        request.setAttribute("totalEspecialidades", dashboardDAO.contarEspecialidades());
        
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
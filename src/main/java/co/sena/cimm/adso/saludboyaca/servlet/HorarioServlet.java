package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.HorarioDAO;
import co.sena.cimm.adso.saludboyaca.dto.Horario;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "HorarioServlet", urlPatterns = {"/horarios"})
public class HorarioServlet extends HttpServlet {

    private HorarioDAO horarioDAO;

    @Override
    public void init() throws ServletException {
        horarioDAO = new HorarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String rol = (String) session.getAttribute("usuarioRol");
        
        List<Horario> horarios;
        
        // Si es médico, solo ve su propio horario
        if ("MEDICO".equals(rol)) {
            horarios = horarioDAO.listarPorMedico(usuario.getId());
        } else {
            // Recepcionista ve todos
            horarios = horarioDAO.listarTodos();
        }
        
        request.setAttribute("horarios", horarios);
        request.setAttribute("menu", "horarios");
        request.getRequestDispatcher("/views/horarios.jsp").forward(request, response);
    }
}
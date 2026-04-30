package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.PacienteDAO;
import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PacienteServlet", urlPatterns = {"/pacientes"})
public class PacienteServlet extends HttpServlet {

    private PacienteDAO pacienteDAO;
    private SimpleDateFormat dateFormat;

    @Override
    public void init() throws ServletException {
        pacienteDAO = new PacienteDAO();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "listar";
        }
        
        switch (accion) {
            case "listar":
                listarPacientes(request, response);
                break;
            case "nuevo":
                mostrarFormulario(request, response, null);
                break;
            case "editar":
                cargarParaEditar(request, response);
                break;
            case "eliminar":
                eliminarPaciente(request, response);
                break;
            default:
                listarPacientes(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String idParam = request.getParameter("id");
        
        Paciente paciente = new Paciente();
        paciente.setNombres(request.getParameter("nombres"));
        paciente.setApellidos(request.getParameter("apellidos"));
        paciente.setDocumento(request.getParameter("documento"));
        paciente.setTelefono(request.getParameter("telefono"));
        paciente.setEmail(request.getParameter("email"));
        paciente.setEps(request.getParameter("eps"));
        paciente.setVeredaBarrio(request.getParameter("veredaBarrio"));
        
        try {
            String fechaStr = request.getParameter("fechaNacimiento");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                paciente.setFechaNacimiento(dateFormat.parse(fechaStr));
            }
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha invalido");
            mostrarFormulario(request, response, paciente);
            return;
        }
        
        boolean exito;
        
        if (idParam != null && !idParam.isEmpty()) {
            // Actualizar
            paciente.setId(Integer.parseInt(idParam));
            exito = pacienteDAO.actualizar(paciente);
        } else {
            // Insertar
            exito = pacienteDAO.insertar(paciente);
        }
        
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/pacientes");
        } else {
            request.setAttribute("error", "Error al guardar el paciente");
            mostrarFormulario(request, response, paciente);
        }
    }

    private void listarPacientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        request.setAttribute("pacientes", pacientes);
        request.setAttribute("menu", "pacientes");
        request.getRequestDispatcher("/views/pacientes/lista.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Paciente paciente)
            throws ServletException, IOException {
        
        request.setAttribute("paciente", paciente);
        request.setAttribute("menu", "pacientes");
        request.getRequestDispatcher("/views/pacientes/formulario.jsp").forward(request, response);
    }

    private void cargarParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Paciente paciente = pacienteDAO.buscarPorId(id);
        
        if (paciente != null) {
            mostrarFormulario(request, response, paciente);
        } else {
            response.sendRedirect(request.getContextPath() + "/pacientes");
        }
    }

    private void eliminarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        pacienteDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/pacientes");
    }
}
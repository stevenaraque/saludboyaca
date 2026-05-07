package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dao.EspecialidadDAO;
import co.sena.cimm.adso.saludboyaca.dao.HorarioDAO;
import co.sena.cimm.adso.saludboyaca.dao.PacienteDAO;
import co.sena.cimm.adso.saludboyaca.dao.UsuarioDAO;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.dto.Especialidad;
import co.sena.cimm.adso.saludboyaca.dto.Horario;
import co.sena.cimm.adso.saludboyaca.dto.Paciente;
import co.sena.cimm.adso.saludboyaca.dto.Usuario;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CitaServlet", urlPatterns = {"/citas"})
public class CitaServlet extends HttpServlet {

    private CitaDAO citaDAO;
    private PacienteDAO pacienteDAO;
    private UsuarioDAO usuarioDAO;
    private EspecialidadDAO especialidadDAO;
    private HorarioDAO horarioDAO;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
        pacienteDAO = new PacienteDAO();
        usuarioDAO = new UsuarioDAO();
        especialidadDAO = new EspecialidadDAO();
        horarioDAO = new HorarioDAO();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";
        
        switch (accion) {
            case "listar":
                listarCitas(request, response);
                break;
            case "nuevo":
                mostrarFormulario(request, response, null);
                break;
            case "editar":
                cargarParaEditar(request, response);
                break;
            case "detalle":
                verDetalle(request, response);
                break;
            case "cambiarEstado":
                cambiarEstadoCita(request, response);
                break;
            case "eliminar":
                eliminarCita(request, response);
                break;
            default:
                listarCitas(request, response);
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    request.setCharacterEncoding("UTF-8");
    String idParam = request.getParameter("id");
    
    Cita cita = new Cita();
    
    try {
        cita.setIdPaciente(Integer.parseInt(request.getParameter("pacienteId")));
        cita.setIdMedico(Integer.parseInt(request.getParameter("medicoId")));
        cita.setIdEspecialidad(Integer.parseInt(request.getParameter("especialidadId")));
        
        String fechaStr = request.getParameter("fechaCita");
        if (fechaStr != null && !fechaStr.isEmpty()) {
            cita.setFechaCita(dateFormat.parse(fechaStr));
        }
        
        String horaStr = request.getParameter("horaCita");
        if (horaStr != null && !horaStr.isEmpty()) {
            cita.setHoraCita(Time.valueOf(horaStr + ":00"));
        }
        
    } catch (ParseException | NumberFormatException e) {
        request.setAttribute("error", "Datos invalidos: " + e.getMessage());
        mostrarFormulario(request, response, cita);
        return;
    }
    
    cita.setMotivo(request.getParameter("motivo"));
    
    // CORREGIDO: respetar el estado enviado desde el formulario
    String estado = request.getParameter("estado");
    cita.setEstado(estado != null && !estado.isEmpty() ? estado : "PROGRAMADA");
    
    HttpSession session = request.getSession();
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    cita.setIdRegistradoPor(usuario.getId());
    
    boolean exito;
    
    if (idParam != null && !idParam.isEmpty()) {
        cita.setId(Integer.parseInt(idParam));
        exito = citaDAO.actualizar(cita);
    } else {
        exito = citaDAO.insertar(cita);
    }
    
    if (exito) {
        response.sendRedirect(request.getContextPath() + "/citas");
    } else {
        request.setAttribute("error", "Error al guardar la cita");
        mostrarFormulario(request, response, cita);
    }
}

    private void listarCitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Cita> citas = citaDAO.listarTodas();
        request.setAttribute("citas", citas);
        request.setAttribute("menu", "citas");
        request.getRequestDispatcher("/views/citas/lista.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Cita cita)
            throws ServletException, IOException {
        
        // Cargar datos para los selects
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        List<Usuario> medicos = usuarioDAO.listarTodos(); // Filtrar solo médicos en la vista
        List<Especialidad> especialidades = especialidadDAO.listarTodas();
        
        request.setAttribute("pacientes", pacientes);
        request.setAttribute("medicos", medicos);
        request.setAttribute("especialidades", especialidades);
        request.setAttribute("cita", cita);
        request.setAttribute("menu", "citas");
        
        request.getRequestDispatcher("/views/citas/formulario.jsp").forward(request, response);
    }

    private void cargarParaEditar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    int id = Integer.parseInt(request.getParameter("id"));
    Cita cita = citaDAO.buscarPorId(id);
    
    if (cita != null) {
        // Formatear fecha para el input type="date" (yyyy-MM-dd)
        if (cita.getFechaCita() != null) {
            String fechaFormateada = dateFormat.format(cita.getFechaCita());
            request.setAttribute("fechaFormateada", fechaFormateada);
        }
        
        // Formatear hora para el input type="time" (HH:mm)
        if (cita.getHoraCita() != null) {
            String horaFormateada = timeFormat.format(cita.getHoraCita());
            request.setAttribute("horaFormateada", horaFormateada);
        }
        
        mostrarFormulario(request, response, cita);
    } else {
        response.sendRedirect(request.getContextPath() + "/citas");
    }
}

    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Cita cita = citaDAO.buscarPorId(id);
        
        if (cita != null) {
            request.setAttribute("cita", cita);
            request.setAttribute("menu", "citas");
            request.getRequestDispatcher("/views/citas/detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/citas");
        }
    }

    private void cambiarEstadoCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String nuevoEstado = request.getParameter("estado");
        
        // Validar estados permitidos
        if (nuevoEstado != null && (
            nuevoEstado.equals("PROGRAMADA") || 
            nuevoEstado.equals("CONFIRMADA") || 
            nuevoEstado.equals("ATENDIDA") || 
            nuevoEstado.equals("CANCELADA"))) {
            
            citaDAO.cambiarEstado(id, nuevoEstado);
        }
        
        response.sendRedirect(request.getContextPath() + "/citas");
    }

    private void eliminarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        citaDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/citas");
    }
}
package co.sena.cimm.adso.saludboyaca.servlet;

import co.sena.cimm.adso.saludboyaca.dao.CitaDAO;
import co.sena.cimm.adso.saludboyaca.dto.Cita;
import co.sena.cimm.adso.saludboyaca.util.PDFGenerator;
import co.sena.cimm.adso.saludboyaca.util.PDFGenerator.TextosPDF;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DescargarComprobanteServlet", urlPatterns = {"/comprobante"})
public class DescargarComprobanteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CitaDAO citaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        citaDAO = new CitaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // ── Determinar si es acceso de personal interno (sesión activa) ──
        boolean esPersonalInterno = session != null
                && session.getAttribute("usuario") != null
                && Boolean.TRUE.equals(session.getAttribute("otpVerificado"));

        // ── Determinar si es acceso público (paciente consultando su propia cita) ──
        // El parámetro "doc" viene de la página de consulta pública
        String docPaciente = request.getParameter("doc");
        boolean esAccesoPublico = (docPaciente != null && !docPaciente.trim().isEmpty());

        // Si no es ninguno de los dos, denegar acceso
        if (!esPersonalInterno && !esAccesoPublico) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no válida");
            return;
        }

        // Si es personal interno, validar rol
        if (esPersonalInterno) {
            String rol = (String) session.getAttribute("usuarioRol");
            if (rol == null || (!rol.equals("MEDICO") && !rol.equals("RECEPCIONISTA"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para descargar comprobantes");
                return;
            }
        }

        // ── Validar parámetro ID ──
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'id' requerido");
            return;
        }

        int idCita;
        try {
            idCita = Integer.parseInt(idParam);
            if (idCita <= 0) {
                throw new NumberFormatException("ID debe ser positivo");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de cita inválido");
            return;
        }

        try {
            Cita cita = citaDAO.buscarPorId(idCita);

            if (cita == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada");
                return;
            }

            // ── Si es acceso público, verificar que la cita pertenece al documento consultado ──
            if (esAccesoPublico) {
                String docCita = cita.getDocumentoPaciente();
                if (docCita == null || !docPaciente.trim().equals(docCita.trim())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado para esta cita");
                    return;
                }
            }

            // ── Obtener idioma ──
            String lang = null;
            if (session != null) {
                lang = (String) session.getAttribute("lang");
            }
            if (lang == null || lang.trim().isEmpty()) {
                lang = "es";
            }

            // ── Cargar textos traducidos y generar PDF ──
            TextosPDF textos = cargarTextos(lang);
            byte[] pdfBytes = PDFGenerator.generarComprobanteCita(cita, textos);

            if (pdfBytes == null || pdfBytes.length == 0) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando PDF vacío");
                return;
            }

            // ── Configurar respuesta HTTP ──
            String nombreArchivo = String.format("comprobante_cita_%d_%s.pdf", idCita, lang);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
            response.setContentLength(pdfBytes.length);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error generando PDF: " + e.getMessage());
        }
    }

    private TextosPDF cargarTextos(String lang) {
        Locale locale;
        switch (lang.toLowerCase()) {
            case "en": locale = Locale.ENGLISH; break;
            case "it": locale = Locale.ITALIAN; break;
            case "es":
            default: locale = new Locale("es"); break;
        }

        ResourceBundle msgs = ResourceBundle.getBundle("messages", locale);
        TextosPDF t = new TextosPDF();

        t.tituloComprobante = getStringSafe(msgs, "cita.comprobante.titulo", "COMPROBANTE DE CITA");
        t.generado          = getStringSafe(msgs, "cita.comprobante.generado", "Generado");
        t.datosPaciente     = getStringSafe(msgs, "cita.datos.paciente", "Datos del Paciente");
        t.nombrePaciente    = getStringSafe(msgs, "paciente.nombres", "Nombres");
        t.documentoPaciente = getStringSafe(msgs, "paciente.documento", "Documento");
        t.infoCita          = getStringSafe(msgs, "cita.info.cita", "Información de la Cita");
        t.medico            = getStringSafe(msgs, "cita.medico", "Médico");
        t.especialidad      = getStringSafe(msgs, "cita.especialidad", "Especialidad");
        t.fecha             = getStringSafe(msgs, "tabla.fecha", "Fecha");
        t.hora              = getStringSafe(msgs, "tabla.hora", "Hora");
        t.estado            = getStringSafe(msgs, "cita.estado", "Estado");
        t.motivo            = getStringSafe(msgs, "cita.motivo", "Motivo");
        t.motivoVacio       = getStringSafe(msgs, "cita.motivo.vacio", "No especificado");
        t.notas             = getStringSafe(msgs, "cita.comprobante.notas", "INFORMACIÓN IMPORTANTE");
        t.nota1             = getStringSafe(msgs, "cita.comprobante.nota1", "Presentarse 15 minutos antes");
        t.nota2             = getStringSafe(msgs, "cita.comprobante.nota2", "Traer documento de identidad");
        t.nota3             = getStringSafe(msgs, "cita.comprobante.nota3", "Comprobante oficial");
        t.valido            = getStringSafe(msgs, "cita.comprobante.valido", "VÁLIDO");
        t.pagina            = getStringSafe(msgs, "cita.comprobante.pagina", "Página");
        t.noDisponible      = getStringSafe(msgs, "tabla.no.disponible", "—");
        t.appNombre         = getStringSafe(msgs, "app.nombre", "SaludBoyaca");
        t.appFooter         = getStringSafe(msgs, "app.footer", "SENA CIMM 2026");

        return t;
    }

    private String getStringSafe(ResourceBundle bundle, String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }
}
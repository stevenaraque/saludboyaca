package co.sena.cimm.adso.saludboyaca.util;

import co.sena.cimm.adso.saludboyaca.dto.Cita;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {
    
    // Paleta SENA exacta
    private static final BaseColor COLOR_AZUL_SENA = new BaseColor(0, 50, 77);      // #00324D
    private static final BaseColor COLOR_VERDE_SENA = new BaseColor(57, 169, 0);      // #39A900
    private static final BaseColor COLOR_GRIS_CLARO = new BaseColor(248, 249, 250);  // #F8F9FA
    private static final BaseColor COLOR_TEXTO = new BaseColor(33, 37, 41);          // #212529
    private static final BaseColor COLOR_TEXTO_MUTED = new BaseColor(108, 117, 125); // #6C757D
    
    /**
     * Genera comprobante de cita médica en PDF
     * 
     * @param cita Datos de la cita
     * @param textos Objeto con todos los textos traducidos
     * @return Array de bytes del PDF generado
     * @throws Exception Si ocurre error en generación
     */
    public static byte[] generarComprobanteCita(Cita cita, TextosPDF textos) throws Exception {
        
        // Validar entrada
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser null");
        }
        if (textos == null) {
            throw new IllegalArgumentException("Los textos no pueden ser null");
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = null;
        
        try {
            document = new Document(PageSize.A4, 50, 50, 60, 50);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            // Agregar evento para header/footer en cada página
            writer.setPageEvent(new PDFHeaderFooterEvent(textos));
            
            document.open();
            
            // Fuentes tipográficas
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.WHITE);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, new BaseColor(200, 220, 230));
            Font fontSeccion = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, COLOR_AZUL_SENA);
            Font fontLabel = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, COLOR_TEXTO);
            Font fontValor = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_TEXTO);
            Font fontNota = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, COLOR_TEXTO_MUTED);
            
            // ===== HEADER INSTITUCIONAL =====
            PdfPTable headerTable = crearHeaderInstitucional(fontTitulo, fontSubtitulo, textos);
            document.add(headerTable);
            
            document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));
            
            // ===== TÍTULO DEL COMPROBANTE =====
            Paragraph tituloComp = new Paragraph(textos.tituloComprobante, fontSeccion);
            tituloComp.setAlignment(Element.ALIGN_CENTER);
            tituloComp.setSpacingAfter(5);
            document.add(tituloComp);
            
            // Fecha de generación
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Paragraph fechaGen = new Paragraph(textos.generado + ": " + sdf.format(new Date()), fontNota);
            fechaGen.setAlignment(Element.ALIGN_CENTER);
            fechaGen.setSpacingAfter(15);
            document.add(fechaGen);
            
            // ===== TABLA DE DATOS =====
            PdfPTable tablaDatos = new PdfPTable(2);
            tablaDatos.setWidthPercentage(100);
            tablaDatos.setSpacingBefore(10);
            tablaDatos.setSpacingAfter(20);
            tablaDatos.setWidths(new float[]{35, 65});
            
            // Datos del paciente
            agregarSeccionTabla(tablaDatos, textos.datosPaciente, COLOR_VERDE_SENA, 2);
            agregarFila(tablaDatos, "ID:", String.valueOf(cita.getId()), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.nombrePaciente + ":", cita.getNombrePaciente(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.documentoPaciente + ":", 
                valorSeguro(cita.getDocumentoPaciente(), textos.noDisponible), fontLabel, fontValor);
            
            // Datos de la cita
            agregarSeccionTabla(tablaDatos, textos.infoCita, COLOR_VERDE_SENA, 2);
            agregarFila(tablaDatos, textos.medico + ":", cita.getNombreMedico(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.especialidad + ":", cita.getNombreEspecialidad(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.fecha + ":", cita.getFechaCita().toString(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.hora + ":", cita.getHoraCita().toString(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.estado + ":", cita.getEstado(), fontLabel, fontValor);
            agregarFila(tablaDatos, textos.motivo + ":", 
                valorSeguro(cita.getMotivo(), textos.motivoVacio), fontLabel, fontValor);
            
            document.add(tablaDatos);
            
            // ===== NOTAS IMPORTANTES =====
            PdfPTable notasTable = new PdfPTable(1);
            notasTable.setWidthPercentage(100);
            notasTable.setSpacingBefore(10);
            
            PdfPCell cellNotas = new PdfPCell();
            cellNotas.setBackgroundColor(COLOR_GRIS_CLARO);
            cellNotas.setPadding(15);
            cellNotas.setBorder(Rectangle.NO_BORDER);
            
            Paragraph tituloNotas = new Paragraph(textos.notas, 
                new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_AZUL_SENA));
            tituloNotas.setSpacingAfter(8);
            cellNotas.addElement(tituloNotas);
            
            String[] notas = {textos.nota1, textos.nota2, textos.nota3};
            
            for (String nota : notas) {
                Paragraph p = new Paragraph("• " + nota, fontNota);
                p.setSpacingAfter(4);
                cellNotas.addElement(p);
            }
            
            notasTable.addCell(cellNotas);
            document.add(notasTable);
            
            // ===== SELLO DE VALIDACIÓN =====
            document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 15)));
            
            PdfPTable selloTable = crearSelloValidacion(textos);
            selloTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            document.add(selloTable);
            
            document.close();
            
            return baos.toByteArray();
            
        } finally {
            // Cerrar recursos
            if (document != null && document.isOpen()) {
                document.close();
            }
            baos.close();
        }
    }
    
    // ===== CLASE POJO PARA TEXTOS =====
    
    /**
     * Clase que contiene todos los textos traducidos para el PDF
     */
    public static class TextosPDF {
        public String tituloComprobante;
        public String generado;
        public String datosPaciente;
        public String nombrePaciente;
        public String documentoPaciente;
        public String infoCita;
        public String medico;
        public String especialidad;
        public String fecha;
        public String hora;
        public String estado;
        public String motivo;
        public String motivoVacio;
        public String notas;
        public String nota1;
        public String nota2;
        public String nota3;
        public String valido;
        public String pagina;
        public String noDisponible;
        public String appNombre;
        public String appFooter;
    }
    
    // ===== MÉTODOS PRIVADOS AUXILIARES =====
    
    /**
     * Crea el header institucional con colores SENA
     */
    private static PdfPTable crearHeaderInstitucional(Font fontTitulo, Font fontSubtitulo, TextosPDF textos) {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_AZUL_SENA);
        cell.setPadding(25);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        
        Paragraph logo = new Paragraph("SALUDBOYACA", fontTitulo);
        logo.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(logo);
        
        Paragraph institucion = new Paragraph(textos.appNombre, fontSubtitulo);
        institucion.setAlignment(Element.ALIGN_CENTER);
        institucion.setSpacingBefore(5);
        cell.addElement(institucion);
        
        header.addCell(cell);
        return header;
    }
    
    /**
     * Crea sello de validación estilizado
     */
    private static PdfPTable crearSelloValidacion(TextosPDF textos) {
        PdfPTable sello = new PdfPTable(1);
        sello.setWidthPercentage(25);
        
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_VERDE_SENA);
        cell.setPadding(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        
        Font fontSello = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        
        Paragraph linea1 = new Paragraph("V " + textos.valido, fontSello);
        linea1.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(linea1);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Paragraph linea2 = new Paragraph(sdf.format(new Date()), fontSello);
        linea2.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(linea2);
        
        sello.addCell(cell);
        return sello;
    }
    
    /**
     * Agrega una sección (header de grupo) a la tabla
     */
    private static void agregarSeccionTabla(PdfPTable tabla, String titulo, BaseColor color, int colspan) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(" " + titulo, font));
        cell.setBackgroundColor(color);
        cell.setPadding(8);
        cell.setColspan(colspan);
        cell.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cell);
    }
    
    /**
     * Agrega una fila label-valor a la tabla
     */
    private static void agregarFila(PdfPTable tabla, String label, String valor, Font fontLabel, Font fontValor) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBackgroundColor(COLOR_GRIS_CLARO);
        cellLabel.setPadding(8);
        cellLabel.setBorderColor(new BaseColor(222, 226, 230));
        cellLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        PdfPCell cellValor = new PdfPCell(new Phrase(valor, fontValor));
        cellValor.setPadding(8);
        cellValor.setBorderColor(new BaseColor(222, 226, 230));
        cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        tabla.addCell(cellLabel);
        tabla.addCell(cellValor);
    }
    
    /**
     * Retorna valor seguro, nunca null
     */
    private static String valorSeguro(String valor, String defecto) {
        return (valor != null && !valor.trim().isEmpty()) ? valor : defecto;
    }
    
    // ===== INNER CLASS: Header/Footer en cada página =====
    
    /**
     * Evento para dibujar header/footer en cada página del PDF
     */
    private static class PDFHeaderFooterEvent extends PdfPageEventHelper {
        
        private final TextosPDF textos;
        private final Font fontFooter;
        private final BaseColor colorAzul;
        
        public PDFHeaderFooterEvent(TextosPDF textos) {
            this.textos = textos;
            this.fontFooter = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, COLOR_TEXTO_MUTED);
            this.colorAzul = COLOR_AZUL_SENA;
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            // Línea decorativa inferior
            cb.setColorStroke(colorAzul);
            cb.setLineWidth(2f);
            cb.moveTo(50, 30);
            cb.lineTo(document.getPageSize().getWidth() - 50, 30);
            cb.stroke();
            
            // Footer con número de página
            Phrase footer = new Phrase(
                textos.appNombre + " | " + textos.appFooter + " | " + textos.pagina + " " + writer.getPageNumber(), 
                fontFooter
            );
            
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                (document.getPageSize().getWidth()) / 2, 15, 0);
        }
    }
}
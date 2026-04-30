package co.sena.cimm.adso.saludboyaca.util;

import co.sena.cimm.adso.saludboyaca.dto.Cita;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.Rectangle;

public class PDFGenerator {
    
    private static final String COLOR_PRIMARIO = "#1A5276";
    private static final String COLOR_SENA = "#39A900";
    
    public static byte[] generarComprobanteCita(Cita cita, String idioma) throws Exception {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Fuentes
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(26, 82, 118));
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
        
        // Header con color institucional
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(new BaseColor(26, 82, 118));
        cellHeader.setPadding(20);
        cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Paragraph titulo = new Paragraph("SALUDBOYACÁ", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph subtitulo = new Paragraph("Centro de Salud Municipal de Paipa", 
            new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE));
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        
        cellHeader.addElement(titulo);
        cellHeader.addElement(subtitulo);
        header.addCell(cellHeader);
        document.add(header);
        
        // Espacio
        document.add(new Paragraph(" "));
        
        // Título del comprobante
        Paragraph tituloComprobante = new Paragraph("COMPROBANTE DE CITA MÉDICA", fontSubtitulo);
        tituloComprobante.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloComprobante);
        
        Paragraph fechaGen = new Paragraph("Generado: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), 
            new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
        fechaGen.setAlignment(Element.ALIGN_CENTER);
        document.add(fechaGen);
        
        document.add(new Paragraph(" "));
        
        // Tabla de datos
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);
        tabla.setWidths(new float[]{1, 2});
        
        // Estilo para labels
        BaseColor colorFondoLabel = new BaseColor(234, 240, 247);
        
        agregarFila(tabla, "Número de Cita:", String.valueOf(cita.getId()), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Paciente:", cita.getNombrePaciente(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Documento:", "Pendiente", colorFondoLabel, fontBold, fontNormal); // Podrías pasar el documento como parámetro
        agregarFila(tabla, "Médico:", cita.getNombreMedico(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Especialidad:", cita.getNombreEspecialidad(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Fecha:", cita.getFechaCita().toString(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Hora:", cita.getHoraCita().toString(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Estado:", cita.getEstado(), colorFondoLabel, fontBold, fontNormal);
        agregarFila(tabla, "Motivo:", cita.getMotivo() != null ? cita.getMotivo() : "No especificado", colorFondoLabel, fontBold, fontNormal);
        
        document.add(tabla);
        
                // Línea separadora
        document.add(new Paragraph(" "));
        PdfPTable linea = new PdfPTable(1);
        linea.setWidthPercentage(100);
        PdfPCell cellLinea = new PdfPCell();
        cellLinea.setBorderColorBottom(new BaseColor(26, 82, 118));
        cellLinea.setBorderWidthBottom(2f);
        cellLinea.setBorder(Rectangle.BOTTOM);
        cellLinea.setPadding(5);
        linea.addCell(cellLinea);
        document.add(linea);
        
        // Footer
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph(
            "Este documento es un comprobante oficial de su cita médica.\n" +
            "Por favor presentarse 15 minutos antes de la hora programada.\n" +
            "Centro de Salud Municipal de Paipa - Boyacá\n" +
            "SENA CIMM - 2026", 
            new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
        
        // Sello verde SENA
        PdfPTable sello = new PdfPTable(1);
        sello.setWidthPercentage(30);
        PdfPCell cellSello = new PdfPCell();
        cellSello.setBackgroundColor(new BaseColor(57, 169, 0));
        cellSello.setPadding(10);
        cellSello.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph pSello = new Paragraph("SENA\nCIMM", 
            new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE));
        pSello.setAlignment(Element.ALIGN_CENTER);
        cellSello.addElement(pSello);
        sello.addCell(cellSello);
        sello.setHorizontalAlignment(Element.ALIGN_RIGHT);
        document.add(sello);
        
        document.close();
        
        return baos.toByteArray();
    }
    
    private static void agregarFila(PdfPTable tabla, String label, String valor, 
            BaseColor colorFondo, Font fontLabel, Font fontValor) {
        
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBackgroundColor(colorFondo);
        cellLabel.setPadding(8);
        cellLabel.setBorderColor(BaseColor.LIGHT_GRAY);
        
        PdfPCell cellValor = new PdfPCell(new Phrase(valor, fontValor));
        cellValor.setPadding(8);
        cellValor.setBorderColor(BaseColor.LIGHT_GRAY);
        
        tabla.addCell(cellLabel);
        tabla.addCell(cellValor);
    }
}
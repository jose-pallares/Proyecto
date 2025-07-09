package com.example.demo.service;

import com.example.demo.models.Product;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class PdfExportService {

    public void exportarProductosA(HttpServletResponse response, List<Product> productos) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_productos.pdf");

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.RED);
            Paragraph titulo = new Paragraph("Reporte de Productos Filtrados", fontTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Tabla
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4f, 2f, 2f});
            tabla.setSpacingBefore(10);

            // Encabezados
            PdfPCell celda = new PdfPCell();
            celda.setBackgroundColor(Color.LIGHT_GRAY);
            celda.setPadding(8);
            Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            celda.setPhrase(new Phrase("Nombre", fuenteCabecera));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase("Stock", fuenteCabecera));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase("Cantidad Vendida", fuenteCabecera));
            tabla.addCell(celda);

            // Filas de productos
            for (Product p : productos) {
                tabla.addCell(p.getName());
                tabla.addCell(String.valueOf(p.getStock()));
                tabla.addCell(String.valueOf(p.getCantidadVendida()));
            }

            document.add(tabla);
            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF: " + e.getMessage());
        }
    }
}

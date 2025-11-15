package com.agrobalance.features.export;

import com.agrobalance.features.model.ReportTable;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.OutputStream;
import java.security.MessageDigest;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PdfExporter {

  public void export(ReportTable table, OutputStream out, String actor) {
    try {
      Document doc = new Document(PageSize.A4.rotate());
      PdfWriter.getInstance(doc, out);
      doc.open();

      // Header
      var title = new Paragraph("Relatório - " + table.getTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
      doc.add(title);
      DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());
      doc.add(new Paragraph("Período: " + fmt.format(table.getFrom()) + " a " + fmt.format(table.getTo())));
      doc.add(new Paragraph("Gerado por: " + (actor == null ? "anonymous" : actor)));
      doc.add(new Paragraph(" "));

      // Table
      PdfPTable pdfTable = new PdfPTable(table.getColumns().size());
      for (String col : table.getColumns()) {
        pdfTable.addCell(new Phrase(col, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
      }
      double[] sums = new double[table.getColumns().size()];
      boolean[] sumCol = new boolean[table.getColumns().size()];
      for (var row : table.getRows()) {
        for (int i = 0; i < row.size(); i++) {
          Object v = row.get(i);
          pdfTable.addCell(v == null ? "" : String.valueOf(v));
          if (v instanceof Number num) {
            sums[i] += num.doubleValue();
            sumCol[i] = true;
          }
        }
      }
      // Totals row (if any numeric)
      boolean hasNumeric = false;
      for (boolean b : sumCol) if (b) { hasNumeric = true; break; }
      if (hasNumeric) {
        for (int i = 0; i < table.getColumns().size(); i++) {
          if (sumCol[i]) pdfTable.addCell(String.format("%.3f", sums[i]));
          else pdfTable.addCell(i == 0 ? "Totais" : "");
        }
      }
      doc.add(pdfTable);

      // Footer with audit hash
      String payload = table.getTitle() + "|" + fmt.format(table.getFrom()) + "|" + fmt.format(table.getTo()) + "|" + table.getColumns() + "|" + table.getRows().hashCode();
      String hash = sha256Hex(payload);
      doc.add(new Paragraph(" "));
      doc.add(new Paragraph("Audit: SHA-256=" + hash + (actor != null ? (" | Actor=" + actor) : ""),
          FontFactory.getFont(FontFactory.COURIER, 9)));

      doc.close();
    } catch (Exception e) {
      throw new RuntimeException("Falha ao exportar PDF", e);
    }
  }

  private String sha256Hex(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] bytes = md.digest(s.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : bytes) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

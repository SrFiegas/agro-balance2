package com.agrobalance.features.export;

import com.agrobalance.features.model.ReportTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CsvExporter {
  public void export(ReportTable table, OutputStream out) {
    try (var writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
         var printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(table.getColumns().toArray(new String[0])))) {
      for (var row : table.getRows()) {
        printer.printRecord(row);
      }
      printer.flush();
    } catch (IOException e) {
      throw new RuntimeException("Falha ao exportar CSV", e);
    }
  }
}

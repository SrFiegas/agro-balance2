package com.agrobalance.features.export;

import com.agrobalance.features.model.ReportTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

public class ExcelExporter {
  public void export(ReportTable table, OutputStream out) {
    try (XSSFWorkbook wb = new XSSFWorkbook()) {
      XSSFSheet sheet = wb.createSheet("Relatorio");
      int r = 0;
      // Header
      Row header = sheet.createRow(r++);
      for (int c = 0; c < table.getColumns().size(); c++) {
        Cell cell = header.createCell(c);
        cell.setCellValue(String.valueOf(table.getColumns().get(c)));
      }
      // Rows
      for (var rowData : table.getRows()) {
        Row row = sheet.createRow(r++);
        for (int c = 0; c < rowData.size(); c++) {
          Object v = rowData.get(c);
          Cell cell = row.createCell(c);
          if (v == null) {
            cell.setBlank();
          } else if (v instanceof Number num) {
            cell.setCellValue(num.doubleValue());
          } else {
            cell.setCellValue(String.valueOf(v));
          }
        }
      }
      for (int c = 0; c < table.getColumns().size(); c++) sheet.autoSizeColumn(c);
      wb.write(out);
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException("Falha ao exportar Excel", e);
    }
  }
}

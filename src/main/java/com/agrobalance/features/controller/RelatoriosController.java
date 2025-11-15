package com.agrobalance.features.controller;

import com.agrobalance.config.AuditorContext;
import com.agrobalance.features.service.RelatoriosService;
import com.agrobalance.features.export.CsvExporter;
import com.agrobalance.features.export.ExcelExporter;
import com.agrobalance.features.export.PdfExporter;
import com.agrobalance.features.model.ReportTable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatoriosController {

  private final RelatoriosService service;

  private final CsvExporter csv = new CsvExporter();
  private final ExcelExporter xlsx = new ExcelExporter();
  private final PdfExporter pdf = new PdfExporter();

  @GetMapping("/saldo")
  public ResponseEntity<?> saldo(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                 @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                 @RequestParam(value = "fazendaId", required = false) Long fazendaId,
                                 @RequestParam(value = "glebaId", required = false) Long glebaId,
                                 @RequestParam(value = "loteId", required = false) Long loteId,
                                 @RequestParam(value = "format", defaultValue = "json") String format,
                                 @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
    validateRange(from, to);
    Map<String, Object> params = new HashMap<>();
    if (fazendaId != null) params.put("fazendaId", fazendaId);
    if (glebaId != null) params.put("glebaId", glebaId);
    if (loteId != null) params.put("loteId", loteId);
    ReportTable table = service.saldoPorLote(from, to, params);
    return render(format, table, ifNoneMatch, "saldo");
  }

  @GetMapping("/recepcao")
  public ResponseEntity<?> recepcao(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                    @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                    @RequestParam(value = "produtoId", required = false) Long produtoId,
                                    @RequestParam(value = "variedadeId", required = false) Long variedadeId,
                                    @RequestParam(value = "cooperadoId", required = false) Long cooperadoId,
                                    @RequestParam(value = "groupBy", defaultValue = "dia") String groupBy,
                                    @RequestParam(value = "format", defaultValue = "json") String format,
                                    @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
    validateRange(from, to);
    if (!groupBy.equals("dia") && !groupBy.equals("semana") && !groupBy.equals("mes")) {
      return ResponseEntity.badRequest().body("groupBy inválido: use dia|semana|mes");
    }
    Map<String, Object> params = new HashMap<>();
    params.put("groupBy", groupBy);
    if (produtoId != null) params.put("produtoId", produtoId);
    if (variedadeId != null) params.put("variedadeId", variedadeId);
    if (cooperadoId != null) params.put("cooperadoId", cooperadoId);
    ReportTable table = service.recepcao(from, to, params);
    return render(format, table, ifNoneMatch, "recepcao");
  }

  @GetMapping("/descontos")
  public ResponseEntity<?> descontos(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                     @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                     @RequestParam(value = "produtoId", required = false) Long produtoId,
                                     @RequestParam(value = "variedadeId", required = false) Long variedadeId,
                                     @RequestParam(value = "groupBy", defaultValue = "produto") String groupBy,
                                     @RequestParam(value = "format", defaultValue = "json") String format,
                                     @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
    validateRange(from, to);
    if (!groupBy.equals("produto") && !groupBy.equals("variedade") && !groupBy.equals("cooperado")) {
      return ResponseEntity.badRequest().body("groupBy inválido: use produto|variedade|cooperado");
    }
    Map<String, Object> params = new HashMap<>();
    params.put("groupBy", groupBy);
    if (produtoId != null) params.put("produtoId", produtoId);
    if (variedadeId != null) params.put("variedadeId", variedadeId);
    ReportTable table = service.descontos(from, to, params);
    return render(format, table, ifNoneMatch, "descontos");
  }

  @GetMapping("/carga")
  public ResponseEntity<?> carga(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                 @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                 @RequestParam(value = "motoristaId", required = false) Long motoristaId,
                                 @RequestParam(value = "format", defaultValue = "json") String format,
                                 @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
    validateRange(from, to);
    Map<String, Object> params = new HashMap<>();
    if (motoristaId != null) params.put("motoristaId", motoristaId);
    ReportTable table = service.carga(from, to, params);
    return render(format, table, ifNoneMatch, "carga");
  }

  @GetMapping("/qualidade")
  public ResponseEntity<?> qualidade(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                     @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                     @RequestParam(value = "produtoId", required = false) Long produtoId,
                                     @RequestParam(value = "format", defaultValue = "json") String format,
                                     @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
    validateRange(from, to);
    Map<String, Object> params = new HashMap<>();
    if (produtoId != null) params.put("produtoId", produtoId);
    ReportTable table = service.qualidade(from, to, params);
    return render(format, table, ifNoneMatch, "qualidade");
  }

  private void validateRange(Instant from, Instant to) {
    if (from == null || to == null || from.isAfter(to)) {
      throw new IllegalArgumentException("Parâmetros 'from' e 'to' são obrigatórios e 'from' deve ser <= 'to'");
    }
  }

  private ResponseEntity<?> render(String format, ReportTable table, String ifNoneMatch, String baseName) {
    format = format == null ? "json" : format.toLowerCase();
    return switch (format) {
      case "csv" -> renderCsv(table, ifNoneMatch, baseName + ".csv");
      case "xlsx" -> renderXlsx(table, ifNoneMatch, baseName + ".xlsx");
      case "pdf" -> renderPdf(table, ifNoneMatch, baseName + ".pdf");
      default -> ResponseEntity.ok(table);
    };
  }

  private ResponseEntity<?> renderCsv(ReportTable table, String ifNoneMatch, String filename) {
    try {
      var out = new ByteArrayOutputStream();
      csv.export(table, out);
      byte[] bytes = out.toByteArray();
      return bytesResponse(bytes, MediaType.TEXT_PLAIN, filename, ifNoneMatch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar CSV");
    }
  }

  private ResponseEntity<?> renderXlsx(ReportTable table, String ifNoneMatch, String filename) {
    try {
      var out = new ByteArrayOutputStream();
      xlsx.export(table, out);
      byte[] bytes = out.toByteArray();
      MediaType mt = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      return bytesResponse(bytes, mt, filename, ifNoneMatch);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar XLSX");
    }
  }

  private ResponseEntity<?> renderPdf(ReportTable table, String ifNoneMatch, String filename) {
    try {
      String etag = tableEtag(table);
      if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
      }
      var out = new ByteArrayOutputStream();
      String actor = AuditorContext.get();
      pdf.export(table, out, actor);
      byte[] bytes = out.toByteArray();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentLength(bytes.length);
      headers.set(HttpHeaders.ETAG, etag);
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
      return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar PDF");
    }
  }

  private String tableEtag(ReportTable table) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      StringBuilder sb = new StringBuilder();
      sb.append(table.getTitle()).append('|')
        .append(table.getFrom()).append('|')
        .append(table.getTo()).append('|')
        .append(String.join(",", table.getColumns())).append('|')
        .append(table.getRows().toString().hashCode());
      byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder();
      for (byte b : digest) hex.append(String.format("%02x", b));
      return hex.toString();
    } catch (Exception e) {
      return (table.getTitle() + table.getColumns() + table.getRows().hashCode()).hashCode() + "";
    }
  }

  private ResponseEntity<?> bytesResponse(byte[] bytes, MediaType mediaType, String filename, String ifNoneMatch) {
    String etag = sha256(bytes);
    if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);
    headers.setContentLength(bytes.length);
    headers.set(HttpHeaders.ETAG, etag);
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
  }

  private String sha256(byte[] bytes) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(bytes);
      StringBuilder sb = new StringBuilder();
      for (byte b : digest) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (Exception e) {
      return new String(bytes, StandardCharsets.UTF_8).hashCode() + ""; // fallback
    }
  }
}

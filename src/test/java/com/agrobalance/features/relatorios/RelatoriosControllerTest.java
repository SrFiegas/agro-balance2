package com.agrobalance.features.relatorios;

import com.agrobalance.features.controller.RelatoriosController;
import com.agrobalance.features.model.ReportTable;
import com.agrobalance.features.service.RelatoriosService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RelatoriosController.class)
@SuppressWarnings("removal")
class RelatoriosControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  RelatoriosService service;

  private ReportTable exampleTable(String title) {
    ReportTable t = new ReportTable();
    t.setTitle(title);
    t.setFrom(Instant.parse("2025-01-01T00:00:00Z"));
    t.setTo(Instant.parse("2025-12-31T23:59:59Z"));
    t.setColumns(List.of("colA", "colB"));
    t.getRows().add(List.of(1, "x"));
    return t;
  }

  @Test
  void saldo_json_ok() throws Exception {
    when(service.saldoPorLote(any(), any(), any())).thenReturn(exampleTable("Saldo por Lote"));

    mvc.perform(get("/api/relatorios/saldo")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Saldo por Lote"))
        .andExpect(jsonPath("$.columns[0]").value("colA"))
        .andExpect(jsonPath("$.rows[0][0]").value(1));
  }

  @Test
  void recepcao_csv_withEtag_and_304_on_replay() throws Exception {
    when(service.recepcao(any(), any(), any())).thenReturn(exampleTable("Recepção"));

    var res1 = mvc.perform(get("/api/relatorios/recepcao")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("groupBy", "dia")
            .param("format", "csv"))
        .andExpect(status().isOk())
        .andExpect(header().exists(HttpHeaders.ETAG))
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andReturn();

    String csv = res1.getResponse().getContentAsString();
    assertThat(csv).contains("colA,colB");
    assertThat(csv).contains("1,x");

    String etag = res1.getResponse().getHeader(HttpHeaders.ETAG);

    mvc.perform(get("/api/relatorios/recepcao")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("groupBy", "dia")
            .param("format", "csv")
            .header(HttpHeaders.IF_NONE_MATCH, etag))
        .andExpect(status().isNotModified());
  }

  @Test
  void descontos_xlsx_ok() throws Exception {
    when(service.descontos(any(), any(), any())).thenReturn(exampleTable("Descontos"));

    var res = mvc.perform(get("/api/relatorios/descontos")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("groupBy", "produto")
            .param("format", "xlsx"))
        .andExpect(status().isOk())
        .andExpect(header().exists(HttpHeaders.ETAG))
        .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .andReturn();

    byte[] bytes = res.getResponse().getContentAsByteArray();
    try (XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
      var sheet = wb.getSheetAt(0);
      assertThat(sheet.getRow(0).getCell(0).getStringCellValue()).isEqualTo("colA");
      assertThat(sheet.getRow(0).getCell(1).getStringCellValue()).isEqualTo("colB");
      assertThat((int) sheet.getRow(1).getCell(0).getNumericCellValue()).isEqualTo(1);
      assertThat(sheet.getRow(1).getCell(1).getStringCellValue()).isEqualTo("x");
    }
  }

  @Test
  void carga_pdf_ok_and_304() throws Exception {
    when(service.carga(any(), any(), any())).thenReturn(exampleTable("Cargas por Viagem"));

    var res = mvc.perform(get("/api/relatorios/carga")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("format", "pdf"))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
        .andExpect(header().exists(HttpHeaders.ETAG))
        .andReturn();

    String etag = res.getResponse().getHeader(HttpHeaders.ETAG);

    mvc.perform(get("/api/relatorios/carga")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("format", "pdf")
            .header(HttpHeaders.IF_NONE_MATCH, etag))
        .andExpect(status().isNotModified());
  }

  @Test
  void qualidade_json_ok() throws Exception {
    when(service.qualidade(any(), any(), any())).thenReturn(exampleTable("Qualidade"));

    mvc.perform(get("/api/relatorios/qualidade")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Qualidade"));
  }

  @Test
  void recepcao_invalid_groupBy_returns400() throws Exception {
    mvc.perform(get("/api/relatorios/recepcao")
            .param("from", "2025-01-01T00:00:00Z")
            .param("to", "2025-12-31T23:59:59Z")
            .param("groupBy", "ano"))
        .andExpect(status().isBadRequest());
  }
}

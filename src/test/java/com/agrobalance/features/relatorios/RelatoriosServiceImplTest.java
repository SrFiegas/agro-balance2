package com.agrobalance.features.relatorios;

import com.agrobalance.features.model.ReportTable;
import com.agrobalance.features.service.impl.RelatoriosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatoriosServiceImplTest {

  @Mock
  NamedParameterJdbcTemplate jdbc;

  RelatoriosServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new RelatoriosServiceImpl(jdbc);
  }

  @Test
  void saldoPorLote_mapsRowsCorrectly() {
    var from = Instant.parse("2025-01-01T00:00:00Z");
    var to = Instant.parse("2025-12-31T23:59:59Z");

    Map<String, Object> row = new HashMap<>();
    row.put("dia", Timestamp.from(from));
    row.put("fazenda_id", 10L);
    row.put("gleba_id", 20L);
    row.put("lote_id", 30L);
    row.put("lote_descricao", "Lote A");
    row.put("entrada_kg", new BigDecimal("100.000"));
    row.put("saida_kg", new BigDecimal("20.000"));
    row.put("saldo_kg", new BigDecimal("80.000"));

    when(jdbc.queryForList(anyString(), ArgumentMatchers.<SqlParameterSource>any()))
        .thenReturn(List.of(row));

    ReportTable table = service.saldoPorLote(from, to, Map.of("loteId", 30L));

    assertThat(table.getTitle()).isEqualTo("Saldo por Lote");
    assertThat(table.getColumns()).containsExactly(
        "dia", "fazendaId", "glebaId", "loteId", "lote", "entradaKg", "saidaKg", "saldoKg");
    assertThat(table.getRows()).hasSize(1);
    List<Object> r = table.getRows().get(0);
    assertThat(r.get(3)).isEqualTo(30L); // loteId
    assertThat(r.get(4)).isEqualTo("Lote A");
    assertThat(new BigDecimal(r.get(7).toString())).isEqualByComparingTo("80.000");
  }

  @Test
  void recepcao_groupByMes_mapsRowsCorrectly() {
    var from = Instant.parse("2025-01-01T00:00:00Z");
    var to = Instant.parse("2025-12-31T23:59:59Z");

    Map<String, Object> row = new HashMap<>();
    row.put("periodo", Timestamp.from(from));
    row.put("produto_id", 1L);
    row.put("variedade_id", 2L);
    row.put("cooperado_id", 3L);
    row.put("total_kg", new BigDecimal("123.456"));

    when(jdbc.queryForList(anyString(), ArgumentMatchers.<SqlParameterSource>any()))
        .thenReturn(List.of(row));

    ReportTable table = service.recepcao(from, to, Map.of("groupBy", "mes", "produtoId", 1L));

    assertThat(table.getTitle()).isEqualTo("Recepção");
    assertThat(table.getColumns()).containsExactly("periodo", "produtoId", "variedadeId", "cooperadoId", "totalKg");
    assertThat(table.getRows()).hasSize(1);
    List<Object> r = table.getRows().get(0);
    assertThat(r.get(1)).isEqualTo(1L);
    assertThat(new BigDecimal(r.get(4).toString())).isEqualByComparingTo("123.456");
  }

  @Test
  void descontos_groupByCooperado_mapsRowsCorrectly() {
    var from = Instant.parse("2025-01-01T00:00:00Z");
    var to = Instant.parse("2025-12-31T23:59:59Z");

    Map<String, Object> row = new HashMap<>();
    row.put("chave", 7L);
    row.put("bruto", new BigDecimal("100.000"));
    row.put("tara", new BigDecimal("10.000"));
    row.put("liquido", new BigDecimal("90.000"));

    when(jdbc.queryForList(anyString(), ArgumentMatchers.<SqlParameterSource>any()))
        .thenReturn(List.of(row));

    ReportTable table = service.descontos(from, to, Map.of("groupBy", "cooperado"));

    assertThat(table.getTitle()).isEqualTo("Descontos");
    assertThat(table.getColumns()).containsExactly("chave", "brutoKg", "taraKg", "liquidoKg");
    assertThat(table.getRows()).hasSize(1);
    List<Object> r = table.getRows().get(0);
    assertThat(r.get(0)).isEqualTo(7L);
    assertThat(new BigDecimal(r.get(3).toString())).isEqualByComparingTo("90.000");
  }

  @Test
  void carga_optionalMotorista_mapsRowsCorrectly() {
    var from = Instant.parse("2025-01-01T00:00:00Z");
    var to = Instant.parse("2025-12-31T23:59:59Z");

    Map<String, Object> row = new HashMap<>();
    row.put("ticket_id", 42L);
    row.put("numero", "202500123");
    row.put("pessoa_motorista_id", 99L);
    row.put("bruto_em", Timestamp.from(from));
    row.put("tara_em", Timestamp.from(to));
    row.put("segundos_viagem", 3600.0);

    when(jdbc.queryForList(anyString(), ArgumentMatchers.<SqlParameterSource>any()))
        .thenReturn(List.of(row));

    ReportTable table = service.carga(from, to, Map.of("motoristaId", 99L));

    assertThat(table.getTitle()).isEqualTo("Cargas por Viagem");
    assertThat(table.getColumns()).containsExactly("ticketId", "numero", "motoristaId", "brutoEm", "taraEm", "segundosViagem");
    assertThat(table.getRows()).hasSize(1);
    List<Object> r = table.getRows().get(0);
    assertThat(r.get(0)).isEqualTo(42L);
    assertThat(r.get(2)).isEqualTo(99L);
    assertThat(Double.parseDouble(r.get(5).toString())).isEqualTo(3600.0);
  }

  @Test
  void qualidade_mapsRowsCorrectly() {
    var from = Instant.parse("2025-01-01T00:00:00Z");
    var to = Instant.parse("2025-12-31T23:59:59Z");

    Map<String, Object> row = new HashMap<>();
    row.put("produto_id", 5L);
    row.put("mes", Timestamp.from(from));
    row.put("umidade_media", new BigDecimal("13.50"));
    row.put("umidade_p95", new BigDecimal("18.00"));
    row.put("impureza_media", new BigDecimal("1.20"));
    row.put("impureza_p95", new BigDecimal("2.50"));

    when(jdbc.queryForList(anyString(), ArgumentMatchers.<SqlParameterSource>any()))
        .thenReturn(List.of(row));

    ReportTable table = service.qualidade(from, to, Map.of("produtoId", 5L));

    assertThat(table.getTitle()).isEqualTo("Qualidade");
    assertThat(table.getColumns()).containsExactly("produtoId", "mes", "umidadeMedia", "umidadeP95", "impurezaMedia", "impurezaP95");
    assertThat(table.getRows()).hasSize(1);
    List<Object> r = table.getRows().get(0);
    assertThat(r.get(0)).isEqualTo(5L);
    assertThat(new BigDecimal(r.get(2).toString())).isEqualByComparingTo("13.50");
    assertThat(new BigDecimal(r.get(5).toString())).isEqualByComparingTo("2.50");
  }
}

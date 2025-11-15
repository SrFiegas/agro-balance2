package com.agrobalance.features.service.impl;

import com.agrobalance.features.model.ReportTable;
import com.agrobalance.features.service.RelatoriosService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RelatoriosServiceImpl implements RelatoriosService {

  private final NamedParameterJdbcTemplate jdbc;

  @Override
  public ReportTable saldoPorLote(Instant from, Instant to, Map<String, Object> params) {
    // Adaptação pós-V13: não há mais movimento/lote; usamos operacao_movimento para sumarizar por dia/fazenda/gleba.
    String timeExpr = "date_trunc('day', created_at)";
    String sql = "SELECT " + timeExpr + " AS dia, fazenda_id, gleba_id, " +
        " NULL::bigint AS lote_id, NULL::varchar AS lote_descricao, " +
        " SUM(COALESCE(peso_liquido,0)) AS entrada_kg, 0::numeric AS saida_kg, SUM(COALESCE(peso_liquido,0)) AS saldo_kg " +
        "FROM operacao_movimento WHERE created_at BETWEEN :from AND :to" +
        opt(" AND fazenda_id = :fazendaId", params, "fazendaId") +
        opt(" AND gleba_id = :glebaId", params, "glebaId") +
        " GROUP BY dia, fazenda_id, gleba_id ORDER BY dia, fazenda_id, gleba_id";
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("from", Timestamp.from(from))
        .addValue("to", Timestamp.from(to));
    bindOptional(p, params, "fazendaId");
    bindOptional(p, params, "glebaId");
    List<Map<String, Object>> rows = jdbc.queryForList(sql, p);

    ReportTable table = new ReportTable();
    table.setTitle("Saldo por Lote");
    table.setFrom(from); table.setTo(to);
    table.setColumns(List.of("dia", "fazendaId", "glebaId", "loteId", "lote", "entradaKg", "saidaKg", "saldoKg"));
    for (Map<String, Object> r : rows) {
      table.getRows().add(List.of(
          r.get("dia"), r.get("fazenda_id"), r.get("gleba_id"), r.get("lote_id"), r.get("lote_descricao"),
          r.get("entrada_kg"), r.get("saida_kg"), r.get("saldo_kg")
      ));
    }
    return table;
  }

  @Override
  public ReportTable recepcao(Instant from, Instant to, Map<String, Object> params) {
    String groupBy = Objects.toString(params.getOrDefault("groupBy", "dia"));
    String timeExpr = switch (groupBy) {
      case "semana" -> "date_trunc('week', created_at)";
      case "mes" -> "date_trunc('month', created_at)";
      default -> "date_trunc('day', created_at)";
    };
    String sql = "SELECT " + timeExpr + " AS periodo, produto_id, variedade_id, pessoa_cooperado_id AS cooperado_id, SUM(COALESCE(peso_liquido,0)) AS total_kg " +
        "FROM operacao_movimento WHERE created_at BETWEEN :from AND :to" +
        opt(" AND produto_id = :produtoId", params, "produtoId") +
        opt(" AND variedade_id = :variedadeId", params, "variedadeId") +
        opt(" AND pessoa_cooperado_id = :cooperadoId", params, "cooperadoId") +
        " GROUP BY periodo, produto_id, variedade_id, pessoa_cooperado_id ORDER BY periodo";
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("from", Timestamp.from(from))
        .addValue("to", Timestamp.from(to));
    bindOptional(p, params, "produtoId");
    bindOptional(p, params, "variedadeId");
    bindOptional(p, params, "cooperadoId");

    List<Map<String, Object>> rows = jdbc.queryForList(sql, p);
    ReportTable table = new ReportTable();
    table.setTitle("Recepção");
    table.setFrom(from); table.setTo(to);
    table.setColumns(List.of("periodo", "produtoId", "variedadeId", "cooperadoId", "totalKg"));
    for (Map<String, Object> r : rows) {
      table.getRows().add(List.of(
          r.get("periodo"), r.get("produto_id"), r.get("variedade_id"), r.get("cooperado_id"), r.get("total_kg")
      ));
    }
    return table;
  }

  @Override
  public ReportTable descontos(Instant from, Instant to, Map<String, Object> params) {
    // Pós-V13: calcular totais a partir de operacao_movimento
    String groupBy = Objects.toString(params.getOrDefault("groupBy", "produto"));
    String groupCols = switch (groupBy) {
      case "variedade" -> "variedade_id";
      case "cooperado" -> "pessoa_cooperado_id";
      default -> "produto_id";
    };
    String sql = "SELECT " + groupCols + " AS chave, " +
        " SUM(COALESCE(peso_bruto,0)) AS bruto, SUM(COALESCE(tara,0)) AS tara, SUM(COALESCE(peso_liquido,0)) AS liquido " +
        "FROM operacao_movimento WHERE created_at BETWEEN :from AND :to" +
        opt(" AND produto_id = :produtoId", params, "produtoId") +
        opt(" AND variedade_id = :variedadeId", params, "variedadeId") +
        " GROUP BY chave ORDER BY chave";
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("from", Timestamp.from(from))
        .addValue("to", Timestamp.from(to));
    bindOptional(p, params, "produtoId");
    bindOptional(p, params, "variedadeId");

    List<Map<String, Object>> rows = jdbc.queryForList(sql, p);
    ReportTable table = new ReportTable();
    table.setTitle("Descontos");
    table.setFrom(from); table.setTo(to);
    table.setColumns(List.of("chave", "brutoKg", "taraKg", "liquidoKg"));
    for (Map<String, Object> r : rows) {
      table.getRows().add(List.of(
          r.get("chave"), r.get("bruto"), r.get("tara"), r.get("liquido")
      ));
    }
    return table;
  }

  @Override
  public ReportTable carga(Instant from, Instant to, Map<String, Object> params) {
    // Pós-V13: dados vêm de operacao_movimento; usamos dthr_peso_bruto e dthr_tara
    String sql = "SELECT id AS ticket_id, nota_fiscal AS numero, motorista_id AS pessoa_motorista_id, " +
        " dthr_peso_bruto AS bruto_em, dthr_tara AS tara_em, " +
        " EXTRACT(EPOCH FROM (COALESCE(dthr_tara, dthr_peso_bruto) - dthr_peso_bruto)) AS segundos_viagem " +
        "FROM operacao_movimento WHERE created_at BETWEEN :from AND :to" +
        opt(" AND motorista_id = :motoristaId", params, "motoristaId") +
        " ORDER BY created_at";
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("from", Timestamp.from(from))
        .addValue("to", Timestamp.from(to));
    bindOptional(p, params, "motoristaId");

    List<Map<String, Object>> rows = jdbc.queryForList(sql, p);
    ReportTable table = new ReportTable();
    table.setTitle("Cargas por Viagem");
    table.setFrom(from); table.setTo(to);
    table.setColumns(List.of("ticketId", "numero", "motoristaId", "brutoEm", "taraEm", "segundosViagem"));
    for (Map<String, Object> r : rows) {
      table.getRows().add(List.of(
          r.get("ticket_id"), r.get("numero"), r.get("pessoa_motorista_id"), r.get("bruto_em"), r.get("tara_em"), r.get("segundos_viagem")
      ));
    }
    return table;
  }

  @Override
  public ReportTable qualidade(Instant from, Instant to, Map<String, Object> params) {
    String sql = "SELECT produto_id, date_trunc('month', created_at) AS mes, " +
        " AVG(COALESCE(perc_umidade,0)) AS umidade_media, " +
        " PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY COALESCE(perc_umidade,0)) AS umidade_p95, " +
        " AVG(COALESCE(perc_impureza,0)) AS impureza_media, " +
        " PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY COALESCE(perc_impureza,0)) AS impureza_p95 " +
        "FROM operacao_movimento WHERE created_at BETWEEN :from AND :to" +
        opt(" AND produto_id = :produtoId", params, "produtoId") +
        " GROUP BY produto_id, mes ORDER BY mes, produto_id";
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("from", Timestamp.from(from))
        .addValue("to", Timestamp.from(to));
    bindOptional(p, params, "produtoId");
    List<Map<String, Object>> rows = jdbc.queryForList(sql, p);
    ReportTable table = new ReportTable();
    table.setTitle("Qualidade");
    table.setFrom(from); table.setTo(to);
    table.setColumns(List.of("produtoId", "mes", "umidadeMedia", "umidadeP95", "impurezaMedia", "impurezaP95"));
    for (Map<String, Object> r : rows) {
      table.getRows().add(List.of(
          r.get("produto_id"), r.get("mes"), r.get("umidade_media"), r.get("umidade_p95"), r.get("impureza_media"), r.get("impureza_p95")
      ));
    }
    return table;
  }

  private String opt(String clause, Map<String, Object> params, String key) {
    Object v = params.get(key);
    if (v == null) return "";
    if (v instanceof String s && s.isBlank()) return "";
    return clause;
  }

  private void bindOptional(MapSqlParameterSource p, Map<String, Object> params, String key) {
    if (params.containsKey(key)) {
      p.addValue(key, params.get(key));
    }
  }
}

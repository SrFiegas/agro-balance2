package com.agrobalance.features.service;

import com.agrobalance.features.model.ReportTable;
import org.springframework.cache.annotation.Cacheable;

import java.time.Instant;
import java.util.Map;

public interface RelatoriosService {

  @Cacheable(value = "reports", key = "'saldo|' + #from + '|' + #to + '|' + #params")
  ReportTable saldoPorLote(Instant from, Instant to, Map<String, Object> params);

  @Cacheable(value = "reports", key = "'recepcao|' + #from + '|' + #to + '|' + #params")
  ReportTable recepcao(Instant from, Instant to, Map<String, Object> params);

  @Cacheable(value = "reports", key = "'descontos|' + #from + '|' + #to + '|' + #params")
  ReportTable descontos(Instant from, Instant to, Map<String, Object> params);

  @Cacheable(value = "reports", key = "'carga|' + #from + '|' + #to + '|' + #params")
  ReportTable carga(Instant from, Instant to, Map<String, Object> params);

  @Cacheable(value = "reports", key = "'qualidade|' + #from + '|' + #to + '|' + #params")
  ReportTable qualidade(Instant from, Instant to, Map<String, Object> params);
}

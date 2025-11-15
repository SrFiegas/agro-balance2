package com.agrobalance.features.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class EstoqueResponseDto {
  private Long id;
  private Long pessoaCooperadoId;
  private Long safraId;
  private Long produtoId;
  private BigDecimal quantidade;

  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;
}

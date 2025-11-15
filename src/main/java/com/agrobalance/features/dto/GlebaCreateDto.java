package com.agrobalance.features.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GlebaCreateDto {
  private String descricao;
  @NotNull
  private Long fazendaId;
}

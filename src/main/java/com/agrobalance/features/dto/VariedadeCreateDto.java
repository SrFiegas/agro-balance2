package com.agrobalance.features.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VariedadeCreateDto {
  @NotBlank
  private String nome;
  @NotNull
  private Long produtoId;
}

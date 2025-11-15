package com.agrobalance.features.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProdutoCreateDto {
  @NotBlank
  private String nome;
}

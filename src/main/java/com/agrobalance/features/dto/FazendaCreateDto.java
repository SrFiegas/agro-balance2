package com.agrobalance.features.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FazendaCreateDto {
  @NotBlank
  private String nome;
}

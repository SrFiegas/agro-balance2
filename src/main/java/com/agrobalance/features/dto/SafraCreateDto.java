package com.agrobalance.features.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SafraCreateDto {
  @NotBlank
  private String nome;
  @NotNull
  private LocalDate dataInicio;
  @NotNull
  private LocalDate dataFim;
}

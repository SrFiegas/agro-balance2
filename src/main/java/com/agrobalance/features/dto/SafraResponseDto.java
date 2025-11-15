package com.agrobalance.features.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class SafraResponseDto {
  private Long id;
  private String nome;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Instant createdAt;
}

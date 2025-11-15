package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "safra")
@Getter
@Setter
public class Safra extends BaseEntity {

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim", nullable = false)
  private LocalDate dataFim;
}

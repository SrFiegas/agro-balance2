package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Deprecated: entidade não utilizada diretamente nas operações atuais.
 * Mantida por compatibilidade com o schema e migrações; candidata à remoção futura.
 */
@Deprecated
@Entity
@Table(name = "cooperado")
@Getter
@Setter
public class Cooperado extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
  private Pessoa pessoa;

  @Column(name = "matricula")
  private Integer matricula;

  @Column(name = "data_admissao")
  private LocalDate dataAdmissao;
}

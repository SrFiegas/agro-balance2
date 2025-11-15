package com.agrobalance.features.domain;

import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.domain.Enum.Status;
import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
public class Pessoa extends BaseEntity {

  @Column(nullable = false)
  private String nome;

  private String documento;
  private String email;
  private String telefone;
  private String cnh;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PessoaTipo tipo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.ATIVO;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fazenda_id")
  private Fazenda fazenda;
}

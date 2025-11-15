package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "estoque")
@Getter
@Setter
public class Estoque extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_cooperado_id")
  private Pessoa cooperado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "safra_id")
  private Safra safra;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "produto_id")
  private Produto produto;

  @Column(name = "quantidade")
  private BigDecimal quantidade;
}

package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gleba")
@Getter
@Setter
public class Gleba extends BaseEntity {
  private String descricao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fazenda_id")
  private Fazenda fazenda;
}

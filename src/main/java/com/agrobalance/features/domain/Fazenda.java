package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fazenda")
@Getter
@Setter
public class Fazenda extends BaseEntity {

  @Column(nullable = false)
  private String nome;
}

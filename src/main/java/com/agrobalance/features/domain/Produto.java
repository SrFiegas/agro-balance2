package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produto")
@Getter
@Setter
public class Produto extends BaseEntity {
  @Column(nullable = false)
  private String nome;
}

package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id")
  private Pessoa pessoa;

  @Column(nullable = false, unique = true, length = 100)
  private String login;

  @Column(nullable = false, length = 255)
  private String senha;

  @Column(name = "nivel_acesso")
  private Integer nivelAcesso;
}

package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transportadora")
@Getter
@Setter
public class Transportadora extends BaseEntity {

  private String nome;
  private String endereco;
  private String cidade;

  @Column(length = 2)
  private String uf;

  private String cep;
  private String telefone;
  private String cnpj;
}

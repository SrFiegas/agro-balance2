package com.agrobalance.features.dto;

import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.domain.Enum.Status;
import lombok.Data;

@Data
public class PessoaUpdateDto {
  private String nome;
  private String documento;
  private String email;
  private String telefone;
  private String cnh;
  private PessoaTipo tipo;
  private Status status;
  private Long fazendaId; // null = não alterar, 0 = remover vinculo, >0 = setar
}

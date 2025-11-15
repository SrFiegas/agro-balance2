package com.agrobalance.features.dto;

import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.domain.Enum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PessoaCreateDto {
  @NotBlank
  private String nome;
  private String documento;
  private String email;
  private String telefone;
  private String cnh;
  @NotNull
  private PessoaTipo tipo;
  @NotNull
  private Status status;
  private Long fazendaId;
}

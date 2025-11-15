package com.agrobalance.features.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class OperacaoMovimentoResponseDto {
  private Long id;
  private Long pessoaCooperadoId;
  private Long produtoId;
  private Long variedadeId;
  private Long fazendaId;
  private Long glebaId;
  private Long transportadoraId;
  private Long motoristaId;
  private Long usuarioId;

  private String placaVeiculo;
  private String placaCarreta;
  private String notaFiscal;

  private BigDecimal pesoBruto;
  private Instant dthrPesoBruto;
  private BigDecimal tara;
  private Instant dthrTara;

  private BigDecimal phInicial;
  private BigDecimal phFinal;
  private BigDecimal percUmidade;
  private BigDecimal percImpureza;
  private BigDecimal percQuireraFisica;
  private BigDecimal percQuireraTabela;
  private BigDecimal percTriguilho;
  private BigDecimal percArdidos;
  private BigDecimal percVerdes;
  private BigDecimal percChochos;
  private BigDecimal percQuebrados;
  private BigDecimal percBrotados;

  private BigDecimal pesoLiquido;
  private BigDecimal custoRecepcaoSecagem;
  private Boolean quebraAplicada;
  private Instant dataRegistro;

  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;
}

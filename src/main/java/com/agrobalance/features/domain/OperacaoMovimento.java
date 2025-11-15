package com.agrobalance.features.domain;

import com.agrobalance.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "operacao_movimento")
@Getter
@Setter
public class OperacaoMovimento extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_cooperado_id")
  private Pessoa cooperado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "produto_id")
  private Produto produto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variedade_id")
  private Variedade variedade;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fazenda_id")
  private Fazenda fazenda;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gleba_id")
  private Gleba gleba;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transportadora_id")
  private Transportadora transportadora;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "motorista_id")
  private Motorista motorista;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @Column(name = "placa_veiculo")
  private String placaVeiculo;

  @Column(name = "placa_carreta")
  private String placaCarreta;

  @Column(name = "nota_fiscal")
  private String notaFiscal;

  @Column(name = "peso_bruto")
  private BigDecimal pesoBruto;

  @Column(name = "dthr_peso_bruto")
  private Instant dthrPesoBruto;

  @Column(name = "tara")
  private BigDecimal tara;

  @Column(name = "dthr_tara")
  private Instant dthrTara;

  @Column(name = "ph_inicial")
  private BigDecimal phInicial;

  @Column(name = "ph_final")
  private BigDecimal phFinal;

  @Column(name = "perc_umidade")
  private BigDecimal percUmidade;

  @Column(name = "perc_impureza")
  private BigDecimal percImpureza;

  @Column(name = "perc_quirera_fisica")
  private BigDecimal percQuireraFisica;

  @Column(name = "perc_quirera_tabela")
  private BigDecimal percQuireraTabela;

  @Column(name = "perc_triguilho")
  private BigDecimal percTriguilho;

  @Column(name = "perc_ardidos")
  private BigDecimal percArdidos;

  @Column(name = "perc_verdes")
  private BigDecimal percVerdes;

  @Column(name = "perc_chochos")
  private BigDecimal percChochos;

  @Column(name = "perc_quebrados")
  private BigDecimal percQuebrados;

  @Column(name = "perc_brotados")
  private BigDecimal percBrotados;

  @Column(name = "peso_liquido")
  private BigDecimal pesoLiquido;

  @Column(name = "custo_recepcao_secagem")
  private BigDecimal custoRecepcaoSecagem;

  @Column(name = "quebra_aplicada")
  private Boolean quebraAplicada;

  @Column(name = "data_registro")
  private Instant dataRegistro;
}

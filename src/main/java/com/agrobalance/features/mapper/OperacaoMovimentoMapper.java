package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.OperacaoMovimento;
import com.agrobalance.features.dto.OperacaoMovimentoCreateDto;
import com.agrobalance.features.dto.OperacaoMovimentoResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface OperacaoMovimentoMapper {

  @Mapping(target = "cooperado.id", source = "pessoaCooperadoId")
  @Mapping(target = "produto.id", source = "produtoId")
  @Mapping(target = "variedade.id", source = "variedadeId")
  @Mapping(target = "fazenda.id", source = "fazendaId")
  @Mapping(target = "gleba.id", source = "glebaId")
  @Mapping(target = "transportadora.id", source = "transportadoraId")
  @Mapping(target = "motorista.id", source = "motoristaId")
  @Mapping(target = "usuario.id", source = "usuarioId")
  OperacaoMovimento toEntity(OperacaoMovimentoCreateDto dto);

  @Mapping(target = "pessoaCooperadoId", source = "cooperado.id")
  @Mapping(target = "produtoId", source = "produto.id")
  @Mapping(target = "variedadeId", source = "variedade.id")
  @Mapping(target = "fazendaId", source = "fazenda.id")
  @Mapping(target = "glebaId", source = "gleba.id")
  @Mapping(target = "transportadoraId", source = "transportadora.id")
  @Mapping(target = "motoristaId", source = "motorista.id")
  @Mapping(target = "usuarioId", source = "usuario.id")
  OperacaoMovimentoResponseDto toDto(OperacaoMovimento e);

  @Mapping(target = "cooperado.id", source = "pessoaCooperadoId")
  @Mapping(target = "produto.id", source = "produtoId")
  @Mapping(target = "variedade.id", source = "variedadeId")
  @Mapping(target = "fazenda.id", source = "fazendaId")
  @Mapping(target = "gleba.id", source = "glebaId")
  @Mapping(target = "transportadora.id", source = "transportadoraId")
  @Mapping(target = "motorista.id", source = "motoristaId")
  @Mapping(target = "usuario.id", source = "usuarioId")
  void update(@MappingTarget OperacaoMovimento e, OperacaoMovimentoCreateDto dto);
}

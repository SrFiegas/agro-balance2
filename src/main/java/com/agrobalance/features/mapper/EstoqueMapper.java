package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Estoque;
import com.agrobalance.features.dto.EstoqueResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface EstoqueMapper {
  @Mapping(target = "pessoaCooperadoId", source = "cooperado.id")
  @Mapping(target = "safraId", source = "safra.id")
  @Mapping(target = "produtoId", source = "produto.id")
  EstoqueResponseDto toDto(Estoque e);
}
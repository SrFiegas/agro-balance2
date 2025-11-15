package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Produto;
import com.agrobalance.features.dto.ProdutoCreateDto;
import com.agrobalance.features.dto.ProdutoResponseDto;
import com.agrobalance.features.dto.ProdutoUpdateDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface ProdutoMapper {
  Produto toEntity(ProdutoCreateDto dto);
  void update(@MappingTarget Produto entity, ProdutoUpdateDto dto);
  ProdutoResponseDto toDto(Produto entity);
}

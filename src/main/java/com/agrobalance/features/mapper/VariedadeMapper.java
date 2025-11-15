package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Variedade;
import com.agrobalance.features.dto.VariedadeCreateDto;
import com.agrobalance.features.dto.VariedadeResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface VariedadeMapper {
  @Mapping(target = "produto.id", source = "produtoId")
  Variedade toEntity(VariedadeCreateDto dto);
  @Mapping(target = "produtoId", source = "produto.id")
  VariedadeResponseDto toDto(Variedade e);
  @Mapping(target = "produto.id", source = "produtoId")
  void update(@MappingTarget Variedade e, VariedadeCreateDto dto);
}

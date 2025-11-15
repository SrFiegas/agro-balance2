package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Fazenda;
import com.agrobalance.features.dto.FazendaCreateDto;
import com.agrobalance.features.dto.FazendaResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface FazendaMapper {
  Fazenda toEntity(FazendaCreateDto dto);
  void update(@MappingTarget Fazenda entity, FazendaCreateDto dto);
  FazendaResponseDto toDto(Fazenda entity);
}

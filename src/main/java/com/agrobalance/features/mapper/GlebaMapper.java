package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Gleba;
import com.agrobalance.features.dto.GlebaCreateDto;
import com.agrobalance.features.dto.GlebaResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface GlebaMapper {
  @Mapping(target = "fazenda.id", source = "fazendaId")
  Gleba toEntity(GlebaCreateDto dto);
  @Mapping(target = "fazendaId", source = "fazenda.id")
  GlebaResponseDto toDto(Gleba e);
  @Mapping(target = "fazenda.id", source = "fazendaId")
  void update(@MappingTarget Gleba e, GlebaCreateDto dto);
}

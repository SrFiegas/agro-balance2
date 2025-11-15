package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Safra;
import com.agrobalance.features.dto.SafraCreateDto;
import com.agrobalance.features.dto.SafraResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface SafraMapper {
  Safra toEntity(SafraCreateDto dto);
  SafraResponseDto toDto(Safra e);
  void update(@org.mapstruct.MappingTarget Safra e, SafraCreateDto dto);
}

package com.agrobalance.features.mapper;

import com.agrobalance.features.domain.Fazenda;
import com.agrobalance.features.domain.Pessoa;
import com.agrobalance.features.dto.PessoaCreateDto;
import com.agrobalance.features.dto.PessoaResponseDto;
import com.agrobalance.shared.mapper.MappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface PessoaMapper {
  // Map fazendaId -> fazenda entity only when provided (null-safe)
  @Mapping(target = "fazenda", source = "fazendaId")
  Pessoa toEntity(PessoaCreateDto dto);

  @Mapping(target = "fazendaId", source = "fazenda.id")
  PessoaResponseDto toDto(Pessoa entity);

  // Avoid modifying relationship here; handled explicitly in service
  @Mapping(target = "fazenda", ignore = true)
  void update(@MappingTarget Pessoa entity, PessoaCreateDto dto);

  // Helper to map id to reference entity
  default Fazenda map(Long id) {
    if (id == null) return null;
    Fazenda f = new Fazenda();
    f.setId(id);
    return f;
  }
}

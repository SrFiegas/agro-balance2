package com.agrobalance.features.service;

import com.agrobalance.features.dto.VariedadeCreateDto;
import com.agrobalance.features.dto.VariedadeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VariedadeService {
  Page<VariedadeResponseDto> list(String q, Pageable pageable);
  VariedadeResponseDto get(Long id);
  VariedadeResponseDto create(VariedadeCreateDto dto);
  VariedadeResponseDto update(Long id, VariedadeCreateDto dto);
  void delete(Long id);
}

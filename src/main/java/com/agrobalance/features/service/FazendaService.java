package com.agrobalance.features.service;

import com.agrobalance.features.dto.FazendaCreateDto;
import com.agrobalance.features.dto.FazendaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FazendaService {
  Page<FazendaResponseDto> list(String q, Pageable pageable);
  FazendaResponseDto get(Long id);
  FazendaResponseDto create(FazendaCreateDto dto);
  FazendaResponseDto update(Long id, FazendaCreateDto dto);
  void delete(Long id);
}

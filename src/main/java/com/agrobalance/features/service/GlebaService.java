package com.agrobalance.features.service;

import com.agrobalance.features.dto.GlebaCreateDto;
import com.agrobalance.features.dto.GlebaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GlebaService {
  Page<GlebaResponseDto> list(String q, Pageable pageable);
  GlebaResponseDto get(Long id);
  GlebaResponseDto create(GlebaCreateDto dto);
  GlebaResponseDto update(Long id, GlebaCreateDto dto);
  void delete(Long id);
}

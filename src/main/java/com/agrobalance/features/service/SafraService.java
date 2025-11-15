package com.agrobalance.features.service;

import com.agrobalance.features.dto.SafraCreateDto;
import com.agrobalance.features.dto.SafraResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SafraService {
  Page<SafraResponseDto> list(String q, Pageable pageable);
  SafraResponseDto get(Long id);
  SafraResponseDto create(SafraCreateDto dto);
  SafraResponseDto update(Long id, SafraCreateDto dto);
  void delete(Long id);
}

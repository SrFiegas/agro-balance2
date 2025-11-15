package com.agrobalance.features.service;

import com.agrobalance.features.dto.ProdutoCreateDto;
import com.agrobalance.features.dto.ProdutoResponseDto;
import com.agrobalance.features.dto.ProdutoUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {
  Page<ProdutoResponseDto> list(String q, Pageable pageable);
  ProdutoResponseDto get(Long id);
  ProdutoResponseDto create(ProdutoCreateDto dto);
  ProdutoResponseDto update(Long id, ProdutoUpdateDto dto);
  void delete(Long id);
}

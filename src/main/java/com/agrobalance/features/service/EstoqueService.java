package com.agrobalance.features.service;

import com.agrobalance.features.dto.EstoqueResponseDto;

import java.util.List;

public interface EstoqueService {
  EstoqueResponseDto get(Long id);
  List<EstoqueResponseDto> list(Long cooperadoId);
}
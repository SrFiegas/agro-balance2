package com.agrobalance.features.service;

import com.agrobalance.features.dto.OperacaoMovimentoCreateDto;
import com.agrobalance.features.dto.OperacaoMovimentoResponseDto;

import java.time.Instant;
import java.util.List;

public interface OperacaoMovimentoService {
  OperacaoMovimentoResponseDto create(OperacaoMovimentoCreateDto dto);
  OperacaoMovimentoResponseDto get(Long id);
  List<OperacaoMovimentoResponseDto> search(Instant from, Instant to, Long cooperadoId, Long produtoId);
}
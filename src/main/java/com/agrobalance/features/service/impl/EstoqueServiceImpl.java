package com.agrobalance.features.service.impl;

import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.dto.EstoqueResponseDto;
import com.agrobalance.features.mapper.EstoqueMapper;
import com.agrobalance.features.repository.EstoqueRepository;
import com.agrobalance.features.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstoqueServiceImpl implements EstoqueService {
  private final EstoqueRepository repository;
  private final EstoqueMapper mapper;

  @Override
  public EstoqueResponseDto get(Long id) {
    var e = repository.findById(id).orElseThrow(() -> new NotFoundException("Estoque não encontrado"));
    return mapper.toDto(e);
    }

  @Override
  public List<EstoqueResponseDto> list(Long cooperadoId) {
    var list = cooperadoId == null ? repository.findAll() : repository.findAllByCooperado_Id(cooperadoId);
    return list.stream().map(mapper::toDto).toList();
  }
}

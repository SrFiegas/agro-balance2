package com.agrobalance.features.service.impl;

import com.agrobalance.exception.BusinessException;
import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Fazenda;
import com.agrobalance.features.dto.FazendaCreateDto;
import com.agrobalance.features.dto.FazendaResponseDto;
import com.agrobalance.features.mapper.FazendaMapper;
import com.agrobalance.features.repository.FazendaRepository;
import com.agrobalance.features.service.FazendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FazendaServiceImpl implements FazendaService {

  private final FazendaRepository repository;
  private final FazendaMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<FazendaResponseDto> list(String q, Pageable pageable) {
    Page<Fazenda> page;
    if (q != null && !q.isBlank()) {
      page = repository.findByNomeContainingIgnoreCase(q, pageable);
    } else {
      page = repository.findAll(pageable);
    }
    List<FazendaResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public FazendaResponseDto get(Long id) {
    Fazenda f = repository.findById(id).orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));
    return mapper.toDto(f);
  }

  @Override
  public FazendaResponseDto create(FazendaCreateDto dto) {
    if (repository.existsByNomeIgnoreCase(dto.getNome())) {
      throw new BusinessException("Já existe fazenda com este nome");
    }
    Fazenda f = mapper.toEntity(dto);
    repository.save(f);
    return mapper.toDto(f);
  }

  @Override
  public FazendaResponseDto update(Long id, FazendaCreateDto dto) {
    Fazenda f = repository.findById(id).orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));
    mapper.update(f, dto);
    repository.save(f);
    return mapper.toDto(f);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException("Fazenda não encontrada");
    }
    repository.deleteById(id);
  }
}

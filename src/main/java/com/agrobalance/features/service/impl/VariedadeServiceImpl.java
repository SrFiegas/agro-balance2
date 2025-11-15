package com.agrobalance.features.service.impl;

import com.agrobalance.exception.BusinessException;
import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Variedade;
import com.agrobalance.features.dto.VariedadeCreateDto;
import com.agrobalance.features.dto.VariedadeResponseDto;
import com.agrobalance.features.mapper.VariedadeMapper;
import com.agrobalance.features.repository.VariedadeRepository;
import com.agrobalance.features.service.VariedadeService;
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
public class VariedadeServiceImpl implements VariedadeService {

  private final VariedadeRepository repository;
  private final VariedadeMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<VariedadeResponseDto> list(String q, Pageable pageable) {
    Page<Variedade> page = (q != null && !q.isBlank())
        ? repository.findByNomeContainingIgnoreCase(q, pageable)
        : repository.findAll(pageable);
    List<VariedadeResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public VariedadeResponseDto get(Long id) {
    Variedade e = repository.findById(id).orElseThrow(() -> new NotFoundException("Variedade não encontrada"));
    return mapper.toDto(e);
  }

  @Override
  public VariedadeResponseDto create(VariedadeCreateDto dto) {
    if (repository.existsByNomeIgnoreCase(dto.getNome())) throw new BusinessException("Já existe variedade com este nome");
    Variedade e = mapper.toEntity(dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public VariedadeResponseDto update(Long id, VariedadeCreateDto dto) {
    Variedade e = repository.findById(id).orElseThrow(() -> new NotFoundException("Variedade não encontrada"));
    mapper.update(e, dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) throw new NotFoundException("Variedade não encontrada");
    repository.deleteById(id);
  }
}

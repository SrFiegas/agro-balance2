package com.agrobalance.features.service.impl;

import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Gleba;
import com.agrobalance.features.dto.GlebaCreateDto;
import com.agrobalance.features.dto.GlebaResponseDto;
import com.agrobalance.features.mapper.GlebaMapper;
import com.agrobalance.features.repository.GlebaRepository;
import com.agrobalance.features.service.GlebaService;
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
public class GlebaServiceImpl implements GlebaService {
  private final GlebaRepository repository;
  private final GlebaMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<GlebaResponseDto> list(String q, Pageable pageable) {
    Page<Gleba> page = (q != null && !q.isBlank())
        ? repository.findByDescricaoContainingIgnoreCase(q, pageable)
        : repository.findAll(pageable);
    List<GlebaResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public GlebaResponseDto get(Long id) {
    Gleba e = repository.findById(id).orElseThrow(() -> new NotFoundException("Gleba não encontrada"));
    return mapper.toDto(e);
  }

  @Override
  public GlebaResponseDto create(GlebaCreateDto dto) {
    Gleba e = mapper.toEntity(dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public GlebaResponseDto update(Long id, GlebaCreateDto dto) {
    Gleba e = repository.findById(id).orElseThrow(() -> new NotFoundException("Gleba não encontrada"));
    mapper.update(e, dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) throw new NotFoundException("Gleba não encontrada");
    repository.deleteById(id);
  }
}

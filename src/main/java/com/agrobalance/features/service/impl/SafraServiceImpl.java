package com.agrobalance.features.service.impl;

import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Safra;
import com.agrobalance.features.dto.SafraCreateDto;
import com.agrobalance.features.dto.SafraResponseDto;
import com.agrobalance.features.mapper.SafraMapper;
import com.agrobalance.features.repository.SafraRepository;
import com.agrobalance.features.service.SafraService;
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
public class SafraServiceImpl implements SafraService {
  private final SafraRepository repository;
  private final SafraMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<SafraResponseDto> list(String q, Pageable pageable) {
    Page<Safra> page = (q != null && !q.isBlank())
        ? repository.findByNomeContainingIgnoreCase(q, pageable)
        : repository.findAll(pageable);
    List<SafraResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public SafraResponseDto get(Long id) {
    Safra e = repository.findById(id).orElseThrow(() -> new NotFoundException("Safra não encontrada"));
    return mapper.toDto(e);
  }

  @Override
  public SafraResponseDto create(SafraCreateDto dto) {
    Safra e = mapper.toEntity(dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public SafraResponseDto update(Long id, SafraCreateDto dto) {
    Safra e = repository.findById(id).orElseThrow(() -> new NotFoundException("Safra não encontrada"));
    mapper.update(e, dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) throw new NotFoundException("Safra não encontrada");
    repository.deleteById(id);
  }
}

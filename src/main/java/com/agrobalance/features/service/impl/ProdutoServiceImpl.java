package com.agrobalance.features.service.impl;

import com.agrobalance.exception.BusinessException;
import com.agrobalance.exception.NotFoundException;
import com.agrobalance.features.domain.Produto;
import com.agrobalance.features.dto.ProdutoCreateDto;
import com.agrobalance.features.dto.ProdutoResponseDto;
import com.agrobalance.features.dto.ProdutoUpdateDto;
import com.agrobalance.features.mapper.ProdutoMapper;
import com.agrobalance.features.repository.ProdutoRepository;
import com.agrobalance.features.service.ProdutoService;
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
public class ProdutoServiceImpl implements ProdutoService {

  private final ProdutoRepository repository;
  private final ProdutoMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Page<ProdutoResponseDto> list(String q, Pageable pageable) {
    Page<Produto> page = (q != null && !q.isBlank())
        ? repository.findByNomeContainingIgnoreCase(q, pageable)
        : repository.findAll(pageable);
    List<ProdutoResponseDto> content = page.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public ProdutoResponseDto get(Long id) {
    Produto e = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    return mapper.toDto(e);
  }

  @Override
  public ProdutoResponseDto create(ProdutoCreateDto dto) {
    if (repository.existsByNomeIgnoreCase(dto.getNome())) throw new BusinessException("Já existe produto com este nome");
    Produto e = mapper.toEntity(dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public ProdutoResponseDto update(Long id, ProdutoUpdateDto dto) {
    Produto e = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

    if (dto.getNome() != null) {
      String newNome = dto.getNome();
      if (!newNome.equalsIgnoreCase(e.getNome()) && repository.existsByNomeIgnoreCase(newNome)) {
        throw new BusinessException("Já existe produto com este nome");
      }
    }

    mapper.update(e, dto);
    repository.save(e);
    return mapper.toDto(e);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) throw new NotFoundException("Produto não encontrado");
    repository.deleteById(id);
  }
}

package com.agrobalance.features.controller;

import com.agrobalance.features.dto.ProdutoCreateDto;
import com.agrobalance.features.dto.ProdutoResponseDto;
import com.agrobalance.features.dto.ProdutoUpdateDto;
import com.agrobalance.features.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
  private final ProdutoService service;

  @GetMapping
  public Page<ProdutoResponseDto> list(@RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return service.list(q, pageable);
  }

  @GetMapping("/{id}")
  public ProdutoResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public ProdutoResponseDto create(@Valid @RequestBody ProdutoCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public ProdutoResponseDto update(@PathVariable Long id, @RequestBody ProdutoUpdateDto dto) { return service.update(id, dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

package com.agrobalance.features.controller;

import com.agrobalance.features.dto.FazendaCreateDto;
import com.agrobalance.features.dto.FazendaResponseDto;
import com.agrobalance.features.service.FazendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fazendas")
@RequiredArgsConstructor
public class FazendaController {
  private final FazendaService service;

  @GetMapping
  public Page<FazendaResponseDto> list(@RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return service.list(q, pageable);
  }

  @GetMapping("/{id}")
  public FazendaResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public FazendaResponseDto create(@Valid @RequestBody FazendaCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public FazendaResponseDto update(@PathVariable Long id, @Valid @RequestBody FazendaCreateDto dto) { return service.update(id, dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

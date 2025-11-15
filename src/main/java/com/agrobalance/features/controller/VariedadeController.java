package com.agrobalance.features.controller;

import com.agrobalance.features.dto.VariedadeCreateDto;
import com.agrobalance.features.dto.VariedadeResponseDto;
import com.agrobalance.features.service.VariedadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variedades")
@RequiredArgsConstructor
public class VariedadeController {
  private final VariedadeService service;

  @GetMapping
  public Page<VariedadeResponseDto> list(@RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return service.list(q, pageable);
  }

  @GetMapping("/{id}")
  public VariedadeResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public VariedadeResponseDto create(@Valid @RequestBody VariedadeCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public VariedadeResponseDto update(@PathVariable Long id, @Valid @RequestBody VariedadeCreateDto dto) { return service.update(id, dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

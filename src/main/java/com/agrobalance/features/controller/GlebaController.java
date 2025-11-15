package com.agrobalance.features.controller;

import com.agrobalance.features.dto.GlebaCreateDto;
import com.agrobalance.features.dto.GlebaResponseDto;
import com.agrobalance.features.service.GlebaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/glebas")
@RequiredArgsConstructor
public class GlebaController {
  private final GlebaService service;

  @GetMapping
  public Page<GlebaResponseDto> list(@RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return service.list(q, pageable);
  }

  @GetMapping("/{id}")
  public GlebaResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public GlebaResponseDto create(@Valid @RequestBody GlebaCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public GlebaResponseDto update(@PathVariable Long id, @Valid @RequestBody GlebaCreateDto dto) { return service.update(id, dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

package com.agrobalance.features.controller;

import com.agrobalance.features.dto.SafraCreateDto;
import com.agrobalance.features.dto.SafraResponseDto;
import com.agrobalance.features.service.SafraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/safras")
@RequiredArgsConstructor
public class SafraController {
  private final SafraService service;

  @GetMapping
  public Page<SafraResponseDto> list(@RequestParam(value = "q", required = false) String q,
                                     Pageable pageable) {
    return service.list(q, pageable);
  }

  @GetMapping("/{id}")
  public SafraResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public SafraResponseDto create(@Valid @RequestBody SafraCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public SafraResponseDto update(@PathVariable Long id, @Valid @RequestBody SafraCreateDto dto) {
    return service.update(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

package com.agrobalance.features.controller;

import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.dto.PessoaCreateDto;
import com.agrobalance.features.dto.PessoaResponseDto;
import com.agrobalance.features.dto.PessoaUpdateDto;
import com.agrobalance.features.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController {
  private final PessoaService service;

  @GetMapping
  public Page<PessoaResponseDto> list(@RequestParam(value = "tipo", required = false) PessoaTipo tipo,
                                      @RequestParam(value = "q", required = false) String q,
                                      Pageable pageable) {
    return service.list(tipo, q, pageable);
  }

  @GetMapping("/{id}")
  public PessoaResponseDto get(@PathVariable Long id) { return service.get(id); }

  @PostMapping
  public PessoaResponseDto create(@Valid @RequestBody PessoaCreateDto dto) { return service.create(dto); }

  @PutMapping("/{id}")
  public PessoaResponseDto update(@PathVariable Long id, @RequestBody PessoaUpdateDto dto) { return service.update(id, dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}

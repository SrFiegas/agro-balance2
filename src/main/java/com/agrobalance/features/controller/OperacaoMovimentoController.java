package com.agrobalance.features.controller;

import com.agrobalance.features.dto.OperacaoMovimentoCreateDto;
import com.agrobalance.features.dto.OperacaoMovimentoResponseDto;
import com.agrobalance.features.service.OperacaoMovimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/operacoes")
@RequiredArgsConstructor
public class OperacaoMovimentoController {
  private final OperacaoMovimentoService service;

  @PostMapping
  public OperacaoMovimentoResponseDto create(@RequestBody OperacaoMovimentoCreateDto dto) {
    return service.create(dto);
  }

  @GetMapping("/{id}")
  public OperacaoMovimentoResponseDto get(@PathVariable Long id) {
    return service.get(id);
  }

  @GetMapping
  public List<OperacaoMovimentoResponseDto> search(
      @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
      @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
      @RequestParam(value = "cooperadoId", required = false) Long cooperadoId,
      @RequestParam(value = "produtoId", required = false) Long produtoId
  ) {
    return service.search(from, to, cooperadoId, produtoId);
  }
}

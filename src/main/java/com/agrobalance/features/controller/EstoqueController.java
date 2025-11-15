package com.agrobalance.features.controller;

import com.agrobalance.features.dto.EstoqueResponseDto;
import com.agrobalance.features.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
@RequiredArgsConstructor
public class EstoqueController {
  private final EstoqueService service;

  @GetMapping
  public List<EstoqueResponseDto> list(@RequestParam(value = "cooperadoId", required = false) Long cooperadoId) {
    return service.list(cooperadoId);
  }

  @GetMapping("/{id}")
  public EstoqueResponseDto get(@PathVariable Long id) {
    return service.get(id);
  }
}

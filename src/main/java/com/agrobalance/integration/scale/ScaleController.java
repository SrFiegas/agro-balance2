package com.agrobalance.integration.scale;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scale")
@RequiredArgsConstructor
public class ScaleController {
  private final ScaleService service;

  @GetMapping("/read")
  public ScaleReading read() {
    return service.read();
  }
}

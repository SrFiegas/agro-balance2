package com.agrobalance.integration.scale;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScaleService {
  private final ScaleProperties props;
  private final SerialScaleAdapter serial;
  private final TcpScaleAdapter tcp;

  public ScaleReading read() {
    String mode = props.getMode();
    if (mode == null || mode.isBlank()) {
      // Fallback seguro para SERIAL se não configurado
      return serial.readStableWeight();
    }
    return "TCP".equalsIgnoreCase(mode) ? tcp.readStableWeight() : serial.readStableWeight();
  }
}

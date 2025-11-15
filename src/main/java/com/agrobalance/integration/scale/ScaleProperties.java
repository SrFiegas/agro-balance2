package com.agrobalance.integration.scale;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("scale")
public class ScaleProperties {
  // Valores: "SERIAL" ou "TCP". Padrão SERIAL.
  private String mode = "SERIAL";
  private Serial serial = new Serial();
  private Tcp tcp = new Tcp();
  // Regex deve conter grupo nomeado (?<value>...) para extrair o peso
  private String parseRegex;
  private String unit = "KG";

  @Getter
  @Setter
  public static class Serial {
    private String port;
    private int baudRate = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private String parity = "NONE";
    private int readTimeoutMillis = 1000;
  }

  @Getter
  @Setter
  public static class Tcp {
    private String host;
    private int port = 5000;
    private int connectTimeoutMillis = 1000;
    private int readTimeoutMillis = 1000;
  }
}

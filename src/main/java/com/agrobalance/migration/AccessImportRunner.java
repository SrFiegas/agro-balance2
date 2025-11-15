package com.agrobalance.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
@Profile("dev")
@ConditionalOnProperty(prefix = "migration.access", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class AccessImportRunner implements CommandLineRunner {

  @Value("${migration.access.url:}")
  private String mdbUrl;

  @Override
  public void run(String... args) throws Exception {
    // Este runner é apenas um esqueleto. Preencha os mapeamentos reais conforme necessidade.
    if (mdbUrl == null || mdbUrl.isBlank()) {
      System.out.println("[AccessImportRunner] migration.access.enabled=true mas 'migration.access.url' não foi definido. Ignorando import.");
      return;
    }
    try (Connection c = DriverManager.getConnection(mdbUrl)) {
      // Exemplo:
      // copyTable(c, "FAZENDAS", rs -> rs.getString("NOME"));
      System.out.println("[AccessImportRunner] Conectado com sucesso ao MDB. Implemente as cópias conforme necessário.");
    } catch (Exception e) {
      // Apenas loga e segue — não deve impedir aplicação de subir em dev
      System.err.println("[AccessImportRunner] Falha ao conectar ao MDB: " + e.getMessage());
    }
  }

  // Crie helpers para mapear colunas do Access para Postgres conforme necessidade real.
}

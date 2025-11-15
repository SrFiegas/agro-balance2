package com.agrobalance.integration;

import com.agrobalance.features.domain.Pessoa;
import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.domain.Enum.Status;
import com.agrobalance.features.repository.PessoaRepository;
import com.agrobalance.features.domain.Produto;
import com.agrobalance.features.repository.ProdutoRepository;
import com.agrobalance.integration.scale.ScaleReading;
import com.agrobalance.integration.scale.ScaleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Disabled;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Disabled("Legacy tickets feature removed in V13; will be replaced by operacao_movimento")
@SuppressWarnings("removal")
class TicketControllerIT {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;
  @Autowired ProdutoRepository produtoRepository;
  @Autowired PessoaRepository pessoaRepository;

  @MockBean ScaleService scaleService;

  @Test
  void full_flow_create_capture_validate_and_pdf() throws Exception {
    // Seed product and driver
    Produto prod = new Produto();
    prod.setNome("Soja");
    produtoRepository.save(prod);

    Pessoa motorista = new Pessoa();
    motorista.setNome("Carlos Motorista");
    motorista.setTipo(PessoaTipo.MOTORISTA);
    motorista.setStatus(Status.ATIVO);
    pessoaRepository.save(motorista);

    var create = new java.util.HashMap<String,Object>();
    create.put("pessoaMotoristaId", motorista.getId());
    create.put("produtoId", prod.getId());

    var resCreate = mvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(create))
            .header("X-Actor-Name", "Tester"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andReturn();

    JsonNode node = om.readTree(resCreate.getResponse().getContentAsString());
    long ticketId = node.get("id").asLong();

    // Mock sequential weight readings: first bruto=1000, then tara=200
    when(scaleService.read())
        .thenReturn(new ScaleReading(1000.0, "KG", true, Instant.now()))
        .thenReturn(new ScaleReading(200.0, "KG", true, Instant.now()));

    mvc.perform(post("/api/tickets/{id}/bruto", ticketId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("BRUTO_CAPTURADO"))
        .andExpect(jsonPath("$.pesoBruto").value(1000.000));

    mvc.perform(post("/api/tickets/{id}/tara", ticketId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("TARA_CAPTURADA"))
        .andExpect(jsonPath("$.pesoTara").value(200.000));

    mvc.perform(post("/api/tickets/{id}/validar", ticketId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("VALIDADO"))
        .andExpect(jsonPath("$.pesoLiquido").value(800.000));

    var pdfRes = mvc.perform(get("/api/tickets/{id}/pdf", ticketId))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/pdf"))
        .andReturn();

    byte[] bytes = pdfRes.getResponse().getContentAsByteArray();
    assertThat(bytes.length).isGreaterThan(100); // some content present
  }
}

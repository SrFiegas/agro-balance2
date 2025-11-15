package com.agrobalance.integration;

import com.agrobalance.features.domain.Pessoa;
import com.agrobalance.features.domain.Enum.PessoaTipo;
import com.agrobalance.features.domain.Enum.Status;
import com.agrobalance.features.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
@Transactional
class PessoaControllerIT {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;
  @Autowired PessoaRepository pessoaRepository;
  @Autowired EntityManager em;

  @Test
  void should_create_update_and_audited_by_headers() throws Exception {
    var create = new java.util.HashMap<String,Object>();
    create.put("nome", "Joao Motorista");
    create.put("tipo", PessoaTipo.MOTORISTA.name());
    create.put("status", Status.ATIVO.name());

    var res = mvc.perform(post("/api/pessoas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(create))
            .header("X-Actor-Id", "u1")
            .header("X-Actor-Name", "Tester"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andReturn();

    Long id = om.readTree(res.getResponse().getContentAsString()).get("id").asLong();

    Pessoa p = pessoaRepository.findById(id).orElseThrow();
    assertThat(p.getCreatedBy()).isEqualTo("u1:Tester");

    // update with new actor header
    create.put("nome", "Joao da Silva");
    mvc.perform(put("/api/pessoas/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(create))
            .header("X-Actor-Id", "u2")
            .header("X-Actor-Name", "Updater"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Joao da Silva"));

    em.clear();
    Pessoa p2 = pessoaRepository.findById(id).orElseThrow();
    assertThat(p2.getUpdatedBy()).as("updatedBy was: %s", p2.getUpdatedBy()).isEqualTo("u2:Updater");
  }
}

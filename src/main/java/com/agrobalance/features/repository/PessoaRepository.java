package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Pessoa;
import com.agrobalance.features.domain.Enum.PessoaTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
  boolean existsByNomeIgnoreCase(String nome);
  Page<Pessoa> findByTipo(PessoaTipo tipo, Pageable pageable);
  Page<Pessoa> findByNomeContainingIgnoreCase(String q, Pageable pageable);
  Page<Pessoa> findByTipoAndNomeContainingIgnoreCase(PessoaTipo tipo, String q, Pageable pageable);
}

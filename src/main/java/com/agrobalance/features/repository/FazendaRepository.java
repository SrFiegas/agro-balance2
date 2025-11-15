package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Fazenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FazendaRepository extends JpaRepository<Fazenda, Long> {
  boolean existsByNomeIgnoreCase(String nome);
  Page<Fazenda> findByNomeContainingIgnoreCase(String q, Pageable pageable);
}

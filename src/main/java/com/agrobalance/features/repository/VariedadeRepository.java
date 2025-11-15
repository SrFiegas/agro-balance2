package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Variedade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariedadeRepository extends JpaRepository<Variedade, Long> {
  boolean existsByNomeIgnoreCase(String nome);
  Page<Variedade> findByNomeContainingIgnoreCase(String q, Pageable pageable);
}

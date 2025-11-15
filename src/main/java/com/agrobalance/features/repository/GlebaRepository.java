package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Gleba;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlebaRepository extends JpaRepository<Gleba, Long> {
  Page<Gleba> findByDescricaoContainingIgnoreCase(String q, Pageable pageable);
}

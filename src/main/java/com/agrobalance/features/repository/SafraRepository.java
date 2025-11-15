package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Safra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafraRepository extends JpaRepository<Safra, Long> {
  Page<Safra> findByNomeContainingIgnoreCase(String q, Pageable pageable);
}

package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
  boolean existsByNomeIgnoreCase(String nome);
  Page<Produto> findByNomeContainingIgnoreCase(String q, Pageable pageable);
}

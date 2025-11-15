package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
  Optional<Estoque> findByCooperado_IdAndSafra_IdAndProduto_Id(Long cooperadoId, Long safraId, Long produtoId);
  List<Estoque> findAllByCooperado_Id(Long cooperadoId);
}
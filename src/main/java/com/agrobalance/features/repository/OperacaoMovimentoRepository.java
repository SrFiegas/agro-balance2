package com.agrobalance.features.repository;

import com.agrobalance.features.domain.OperacaoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OperacaoMovimentoRepository extends JpaRepository<OperacaoMovimento, Long> {

  List<OperacaoMovimento> findAllByCooperado_Id(Long cooperadoId);

  List<OperacaoMovimento> findAllByProduto_Id(Long produtoId);

  List<OperacaoMovimento> findAllByCreatedAtBetween(Instant from, Instant to);

  @Query("select o from OperacaoMovimento o where (:cooperadoId is null or o.cooperado.id = :cooperadoId) " +
      "and (:produtoId is null or o.produto.id = :produtoId) and o.createdAt between :from and :to order by o.createdAt desc")
  List<OperacaoMovimento> search(
      @Param("from") Instant from,
      @Param("to") Instant to,
      @Param("cooperadoId") Long cooperadoId,
      @Param("produtoId") Long produtoId);
}
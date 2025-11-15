package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Cooperado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Deprecated: não está sendo utilizada diretamente no código.
 * Sugerido remover em uma próxima limpeza após confirmar em produção.
 */
@Deprecated
@NoRepositoryBean
public interface CooperadoRepository extends JpaRepository<Cooperado, Long> {
  Optional<Cooperado> findByPessoa_Id(Long pessoaId);
  boolean existsByPessoa_Id(Long pessoaId);
}
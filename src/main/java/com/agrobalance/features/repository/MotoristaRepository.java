package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Deprecated: não está sendo utilizada diretamente no código.
 * Sugerido remover em uma próxima limpeza após confirmar em produção.
 */
@Deprecated
@NoRepositoryBean
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
  Optional<Motorista> findByPessoa_Id(Long pessoaId);
  boolean existsByPessoa_Id(Long pessoaId);
}
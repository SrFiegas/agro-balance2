package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Deprecated: não está sendo utilizada diretamente no código.
 * Sugerido remover em uma próxima limpeza após confirmar em produção.
 */
@Deprecated
@NoRepositoryBean
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {
  boolean existsByCnpj(String cnpj);
}
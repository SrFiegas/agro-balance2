package com.agrobalance.features.repository;

import com.agrobalance.features.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Deprecated: not used in the current codebase. Kept only to avoid breaking changes.
 * Prefer removal in a follow-up cleanup when confirmed unused in deployments.
 */
@Deprecated
@NoRepositoryBean
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByLogin(String login);
  boolean existsByLogin(String login);
}
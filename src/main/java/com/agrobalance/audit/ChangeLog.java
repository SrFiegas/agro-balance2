package com.agrobalance.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "entity")
  private String entity;

  @Column(name = "entity_id")
  private String entityId;

  @Column(name = "action")
  private String action;

  // JSONB no Postgres; mapeado via tipo JSON do Hibernate 6
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "changes", columnDefinition = "jsonb")
  private String changes;

  @Column(name = "actor")
  private String actor;

  @Column(name = "created_at")
  private Instant createdAt = Instant.now();
}

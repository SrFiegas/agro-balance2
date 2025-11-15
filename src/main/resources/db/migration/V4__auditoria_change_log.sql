CREATE TABLE audit_log (
  id BIGSERIAL PRIMARY KEY,
  entity VARCHAR(150) NOT NULL,
  entity_id VARCHAR(100) NOT NULL,
  action VARCHAR(20) NOT NULL,
  changes JSONB,
  actor VARCHAR(200),
  created_at TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_audit_log_entity ON audit_log(entity, entity_id);

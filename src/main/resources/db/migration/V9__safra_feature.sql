CREATE TABLE IF NOT EXISTS safra (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(120) NOT NULL,
  data_inicio DATE NOT NULL,
  data_fim DATE NOT NULL,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);
-- opcional: associar ticket à safra (se for explícito)
ALTER TABLE ticket ADD COLUMN IF NOT EXISTS safra_id BIGINT REFERENCES safra(id);

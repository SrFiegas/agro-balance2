-- Adiciona colunas de auditoria exigidas pelo BaseEntity em peso_medicao
ALTER TABLE peso_medicao
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP,
  ADD COLUMN IF NOT EXISTS created_by VARCHAR(200),
  ADD COLUMN IF NOT EXISTS updated_by VARCHAR(200);

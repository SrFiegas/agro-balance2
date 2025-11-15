CREATE TABLE IF NOT EXISTS secagem (
  id BIGSERIAL PRIMARY KEY,
  lote_id BIGINT REFERENCES lote_armazenagem(id),
  umidade_inicial NUMERIC(5,2),
  umidade_final NUMERIC(5,2),
  temperatura_media NUMERIC(5,2),
  horas_operacao NUMERIC(7,2),
  observacao TEXT,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);
CREATE INDEX IF NOT EXISTS idx_secagem_lote ON secagem(lote_id);

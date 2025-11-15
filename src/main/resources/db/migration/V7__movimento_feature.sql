CREATE TYPE movimento_tipo AS ENUM ('ENTRADA','SAIDA');
CREATE TABLE IF NOT EXISTS movimento (
  id BIGSERIAL PRIMARY KEY,
  tipo movimento_tipo NOT NULL,
  quantidade NUMERIC(15,3) NOT NULL,
  lote_id BIGINT REFERENCES lote_armazenagem(id),
  ticket_id BIGINT REFERENCES ticket(id),
  observacao TEXT,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);
CREATE INDEX IF NOT EXISTS idx_movimento_lote ON movimento(lote_id);
CREATE INDEX IF NOT EXISTS idx_movimento_ticket ON movimento(ticket_id);

-- View dependente da tabela movimento (movido de V6 para evitar erro de ordem)
CREATE OR REPLACE VIEW vw_movimento_saldo AS
SELECT
  l.id AS lote_id,
  l.descricao AS lote_descricao,
  g.id AS gleba_id,
  f.id AS fazenda_id,
  date_trunc('day', COALESCE(m.created_at, l.created_at)) AS dia,
  COALESCE(SUM(CASE WHEN m.tipo='ENTRADA' THEN m.quantidade ELSE 0 END),0) AS entrada_kg,
  COALESCE(SUM(CASE WHEN m.tipo='SAIDA'   THEN m.quantidade ELSE 0 END),0) AS saida_kg,
  COALESCE(SUM(CASE WHEN m.tipo='ENTRADA' THEN m.quantidade ELSE -m.quantidade END),0) AS saldo_kg
FROM lote_armazenagem l
LEFT JOIN gleba g ON g.id = l.gleba_id
LEFT JOIN fazenda f ON f.id = g.fazenda_id
LEFT JOIN movimento m ON m.lote_id = l.id
GROUP BY 1,2,3,4,5;

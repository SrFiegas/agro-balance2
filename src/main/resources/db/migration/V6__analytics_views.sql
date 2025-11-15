-- Views para relatórios complexos (canônicas)
CREATE OR REPLACE VIEW vw_ticket_pesagens AS
SELECT
  t.id AS ticket_id,
  t.numero,
  t.status,
  t.fazenda_id, t.gleba_id, t.lote_id,
  t.produto_id, t.variedade_id,
  t.pessoa_motorista_id, t.cooperado_id,
  COALESCE((SELECT valor FROM peso_medicao pm WHERE pm.ticket_id=t.id AND pm.tipo='BRUTO' ORDER BY pm.capturado_em DESC LIMIT 1),0) AS peso_bruto,
  COALESCE((SELECT valor FROM peso_medicao pm WHERE pm.ticket_id=t.id AND pm.tipo='TARA'  ORDER BY pm.capturado_em DESC LIMIT 1),0) AS peso_tara,
  GREATEST(0, 
    COALESCE((SELECT valor FROM peso_medicao pm WHERE pm.ticket_id=t.id AND pm.tipo='BRUTO' ORDER BY pm.capturado_em DESC LIMIT 1),0) -
    COALESCE((SELECT valor FROM peso_medicao pm WHERE pm.ticket_id=t.id AND pm.tipo='TARA'  ORDER BY pm.capturado_em DESC LIMIT 1),0)
  ) AS peso_liquido,
  t.created_at, t.updated_at
FROM ticket t;


CREATE OR REPLACE VIEW vw_qualidade_resumo AS
SELECT
  pq.produto_id,
  DATE_TRUNC('month', COALESCE(pq.updated_at, pq.created_at)) AS mes,
  AVG(COALESCE(pq.umidade_percent,0))  AS umidade_media,
  PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY COALESCE(pq.umidade_percent,0))  AS umidade_p95,
  AVG(COALESCE(pq.impureza_percent,0)) AS impureza_media,
  PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY COALESCE(pq.impureza_percent,0)) AS impureza_p95
FROM parametros_qualidade pq
GROUP BY 1,2;

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_peso_medicao_ticket_tipo ON peso_medicao(ticket_id, tipo);
CREATE INDEX IF NOT EXISTS idx_ticket_created_at ON ticket(created_at);
CREATE INDEX IF NOT EXISTS idx_lote_created_at ON lote_armazenagem(created_at);

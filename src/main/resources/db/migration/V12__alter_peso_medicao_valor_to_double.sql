-- Altera o tipo da coluna 'valor' de NUMERIC(15,3) para DOUBLE PRECISION para alinhar com a entidade (Double)
DROP VIEW IF EXISTS vw_ticket_pesagens;

ALTER TABLE peso_medicao
  ALTER COLUMN valor TYPE DOUBLE PRECISION USING valor::double precision;

-- Recria a view dependente
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
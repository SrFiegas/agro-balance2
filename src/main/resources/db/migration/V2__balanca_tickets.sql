-- Equipamentos/Config da balança
CREATE TABLE escala_dispositivo (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  modo VARCHAR(10) NOT NULL,
  endereco VARCHAR(100),
  porta INTEGER,
  baud_rate INTEGER,
  data_bits INTEGER,
  stop_bits INTEGER,
  parity VARCHAR(10),
  parse_regex VARCHAR(200),
  unit VARCHAR(5) DEFAULT 'KG',
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- Ticket de pesagem (romaneio)
CREATE TABLE ticket (
  id BIGSERIAL PRIMARY KEY,
  numero VARCHAR(30) UNIQUE,
  status ticket_status NOT NULL DEFAULT 'CRIADO',
  pessoa_motorista_id BIGINT REFERENCES pessoa(id),
  cooperado_id BIGINT REFERENCES pessoa(id),
  produto_id BIGINT REFERENCES produto(id),
  variedade_id BIGINT REFERENCES variedade(id),
  fazenda_id BIGINT REFERENCES fazenda(id),
  gleba_id BIGINT REFERENCES gleba(id),
  lote_id BIGINT REFERENCES lote_armazenagem(id),
  observacao TEXT,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- Medições ligadas ao ticket
CREATE TABLE peso_medicao (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES ticket(id) ON DELETE CASCADE,
  tipo VARCHAR(10) NOT NULL,
  valor NUMERIC(15,3) NOT NULL,
  capturado_em TIMESTAMP DEFAULT NOW()
);

-- Parâmetros de desconto/umidade/impureza
CREATE TABLE parametros_qualidade (
  id BIGSERIAL PRIMARY KEY,
  produto_id BIGINT REFERENCES produto(id),
  umidade_percent NUMERIC(5,2) DEFAULT 0.00,
  impureza_percent NUMERIC(5,2) DEFAULT 0.00,
  quebra_tec_percent NUMERIC(5,2) DEFAULT 0.00,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

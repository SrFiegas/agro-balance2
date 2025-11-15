CREATE TABLE recepcao (
  id BIGSERIAL PRIMARY KEY,
  data TIMESTAMP DEFAULT NOW(),
  ticket_id BIGINT REFERENCES ticket(id),
  pessoa_motorista_id BIGINT REFERENCES pessoa(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE expedicao (
  id BIGSERIAL PRIMARY KEY,
  data TIMESTAMP DEFAULT NOW(),
  ticket_id BIGINT REFERENCES ticket(id),
  pessoa_motorista_id BIGINT REFERENCES pessoa(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

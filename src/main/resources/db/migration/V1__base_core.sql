-- Tipos
CREATE TYPE pessoa_tipo AS ENUM ('COOPERADO','MOTORISTA','FORNECEDOR','FUNCIONARIO','OUTRO');
CREATE TYPE status_enum  AS ENUM ('ATIVO','INATIVO');
CREATE TYPE ticket_status AS ENUM ('CRIADO','BRUTO_CAPTURADO','TARA_CAPTURADA','VALIDADO','IMPRESSO','CANCELADO');

-- Centros
CREATE TABLE fazenda (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE pessoa (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  documento VARCHAR(20),
  email VARCHAR(255),
  telefone VARCHAR(50),
  cnh VARCHAR(64),
  tipo pessoa_tipo NOT NULL,
  status status_enum NOT NULL DEFAULT 'ATIVO',
  fazenda_id BIGINT REFERENCES fazenda(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE produto (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE variedade (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  produto_id BIGINT REFERENCES produto(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE gleba (
  id BIGSERIAL PRIMARY KEY,
  descricao VARCHAR(255),
  fazenda_id BIGINT REFERENCES fazenda(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

CREATE TABLE lote_armazenagem (
  id BIGSERIAL PRIMARY KEY,
  descricao VARCHAR(255),
  gleba_id BIGINT REFERENCES gleba(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

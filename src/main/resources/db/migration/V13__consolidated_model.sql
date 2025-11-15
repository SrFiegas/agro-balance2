-- Consolidação do modelo conforme prompt do usuário
-- Estratégia: manter tabelas centrais existentes (pessoa, produto, variedade, fazenda, gleba, safra)
-- adicionando colunas necessárias; criar novas tabelas; remover estruturas legadas
-- que não fazem parte do novo modelo (ticket/peso_medicao/etc.).
-- Observação: este script é destrutivo para módulos legados.

-- 1) Remoção de views dependentes de estruturas legadas
DROP VIEW IF EXISTS vw_ticket_pesagens;
DROP VIEW IF EXISTS vw_qualidade_resumo;
DROP VIEW IF EXISTS vw_movimento_saldo;

-- 2) Remoção de tabelas legadas (ordem cuidadosa por FKs)
-- Tabelas que referenciam ticket/lote primeiro
DROP TABLE IF EXISTS recepcao;
DROP TABLE IF EXISTS expedicao;
DROP TABLE IF EXISTS peso_medicao;
DROP TABLE IF EXISTS movimento;
DROP TABLE IF EXISTS secagem;
-- Tabelas de domínio legadas
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS lote_armazenagem;
DROP TABLE IF EXISTS escala_dispositivo;
DROP TABLE IF EXISTS parametros_qualidade;

-- 3) Remover tipos ENUM legados não mais utilizados
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movimento_tipo') THEN
    EXECUTE 'DROP TYPE movimento_tipo';
  END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'ticket_status') THEN
    EXECUTE 'DROP TYPE ticket_status';
  END IF;
END $$;

-- 4) Ajustes nas tabelas existentes para acomodar o novo modelo
-- pessoa: adicionar campos de endereço/documentos/observações
ALTER TABLE pessoa
  ADD COLUMN IF NOT EXISTS endereco VARCHAR(255),
  ADD COLUMN IF NOT EXISTS bairro VARCHAR(100),
  ADD COLUMN IF NOT EXISTS cidade VARCHAR(100),
  ADD COLUMN IF NOT EXISTS uf VARCHAR(2),
  ADD COLUMN IF NOT EXISTS cep VARCHAR(20),
  ADD COLUMN IF NOT EXISTS celular VARCHAR(50),
  ADD COLUMN IF NOT EXISTS fax VARCHAR(50),
  ADD COLUMN IF NOT EXISTS cpf VARCHAR(20),
  ADD COLUMN IF NOT EXISTS rg VARCHAR(50),
  ADD COLUMN IF NOT EXISTS data_nascimento DATE,
  ADD COLUMN IF NOT EXISTS observacao TEXT;

-- produto/variedade: adicionar colunas de descricao
ALTER TABLE produto
  ADD COLUMN IF NOT EXISTS descricao VARCHAR(255);

ALTER TABLE variedade
  ADD COLUMN IF NOT EXISTS descricao VARCHAR(255);

-- fazenda: área (hectares) e responsável (pessoa)
ALTER TABLE fazenda
  ADD COLUMN IF NOT EXISTS num_ha NUMERIC(12,2),
  ADD COLUMN IF NOT EXISTS pessoa_responsavel_id BIGINT REFERENCES pessoa(id);

-- gleba: nome e área
ALTER TABLE gleba
  ADD COLUMN IF NOT EXISTS nome VARCHAR(255),
  ADD COLUMN IF NOT EXISTS num_ha NUMERIC(12,2);

-- safra: campos de descricao e ano
ALTER TABLE safra
  ADD COLUMN IF NOT EXISTS descricao VARCHAR(255),
  ADD COLUMN IF NOT EXISTS ano INTEGER;

-- 5) Novas tabelas específicas conforme modelo

-- cooperado
CREATE TABLE IF NOT EXISTS cooperado (
  id BIGSERIAL PRIMARY KEY,
  pessoa_id BIGINT NOT NULL UNIQUE REFERENCES pessoa(id),
  matricula INTEGER,
  data_admissao DATE,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- usuario (controle de acesso simples)
CREATE TABLE IF NOT EXISTS usuario (
  id BIGSERIAL PRIMARY KEY,
  pessoa_id BIGINT REFERENCES pessoa(id),
  login VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  nivel_acesso INTEGER,
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- motorista
CREATE TABLE IF NOT EXISTS motorista (
  id BIGSERIAL PRIMARY KEY,
  pessoa_id BIGINT NOT NULL UNIQUE REFERENCES pessoa(id),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- transportadora
CREATE TABLE IF NOT EXISTS transportadora (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255),
  endereco VARCHAR(255),
  cidade VARCHAR(100),
  uf VARCHAR(2),
  cep VARCHAR(20),
  telefone VARCHAR(50),
  cnpj VARCHAR(20),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- operacao_movimento (registro de pesagem e qualidade; centraliza "quebra técnica")
CREATE TABLE IF NOT EXISTS operacao_movimento (
  id BIGSERIAL PRIMARY KEY,
  pessoa_cooperado_id BIGINT REFERENCES pessoa(id),
  produto_id BIGINT REFERENCES produto(id),
  variedade_id BIGINT REFERENCES variedade(id),
  fazenda_id BIGINT REFERENCES fazenda(id),
  gleba_id BIGINT REFERENCES gleba(id),
  transportadora_id BIGINT REFERENCES transportadora(id),
  motorista_id BIGINT REFERENCES motorista(id),
  usuario_id BIGINT REFERENCES usuario(id),
  placa_veiculo VARCHAR(20),
  placa_carreta VARCHAR(20),
  nota_fiscal VARCHAR(100),
  peso_bruto NUMERIC(14,3),
  dthr_peso_bruto TIMESTAMP,
  tara NUMERIC(14,3),
  dthr_tara TIMESTAMP,
  ph_inicial NUMERIC(8,3),
  ph_final NUMERIC(8,3),
  perc_umidade NUMERIC(8,3),
  perc_impureza NUMERIC(8,3),
  perc_quirera_fisica NUMERIC(8,3),
  perc_quirera_tabela NUMERIC(8,3),
  perc_triguilho NUMERIC(8,3),
  perc_ardidos NUMERIC(8,3),
  perc_verdes NUMERIC(8,3),
  perc_chochos NUMERIC(8,3),
  perc_quebrados NUMERIC(8,3),
  perc_brotados NUMERIC(8,3),
  peso_liquido NUMERIC(14,3),
  custo_recepcao_secagem NUMERIC(14,2),
  quebra_aplicada BOOLEAN,
  data_registro TIMESTAMP DEFAULT NOW(),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- estoque (resumo por cooperado/safra/produto)
CREATE TABLE IF NOT EXISTS estoque (
  id BIGSERIAL PRIMARY KEY,
  pessoa_cooperado_id BIGINT REFERENCES pessoa(id),
  safra_id BIGINT REFERENCES safra(id),
  produto_id BIGINT REFERENCES produto(id),
  quantidade NUMERIC(18,3),
  created_at TIMESTAMP, updated_at TIMESTAMP, created_by VARCHAR(200), updated_by VARCHAR(200)
);

-- índices úteis
CREATE INDEX IF NOT EXISTS idx_estoque_chave ON estoque(pessoa_cooperado_id, safra_id, produto_id);

-- 6) Auditoria detalhada no formato solicitado (mantém audit_log atual)
CREATE TABLE IF NOT EXISTS audit_log_detalhado (
  id BIGSERIAL PRIMARY KEY,
  table_name VARCHAR(100) NOT NULL,
  operation VARCHAR(10) NOT NULL,
  key_values JSONB,
  changed_by_usuario BIGINT,
  changed_at TIMESTAMP DEFAULT NOW(),
  old_value JSONB,
  new_value JSONB,
  details TEXT
);

-- Fim do script de consolidação

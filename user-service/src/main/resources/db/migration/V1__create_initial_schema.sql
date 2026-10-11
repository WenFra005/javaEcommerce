-- ==================================================
-- Migration: V1__create_initial_schema.sql
-- Description: Cria todas as tabelas do sistema
-- Author: Wendell Francisco
-- Date: 2024-06-20
-- ==================================================
-- 1. Tabela de usuários
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    user_status VARCHAR(50) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    user_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (user_email),
    CONSTRAINT ck_user_role CHECK (
        user_role IN ('ADMIN', 'CLIENTE', 'VENDEDOR', 'FORNECEDOR')
    ),
    CONSTRAINT ck_user_status CHECK (user_status IN ('ATIVO', 'INATIVO', 'SUSPENSO')),
    CONSTRAINT ck_user_type CHECK (user_type IN ('PF', 'PJ', 'SYSTEM'))
);
CREATE INDEX idx_users_email ON users(user_email);
CREATE INDEX idx_users_role ON users(user_role);
CREATE INDEX idx_users_status ON users(user_status);
CREATE INDEX idx_users_type ON users(user_type);
-- 2. Tabela de pessoas físicas
CREATE TABLE IF NOT EXISTS natural_persons (
    user_id BIGINT NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    birth_date DATE NULL,
    CONSTRAINT pk_natural_persons PRIMARY KEY (user_id),
    CONSTRAINT fk_natural_persons_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uk_natural_persons_cpf UNIQUE (cpf)
);
CREATE INDEX idx_natural_persons_cpf ON natural_persons(cpf);
-- 3. Tabela de pessoas jurídicas
CREATE TABLE IF NOT EXISTS legal_entities (
    user_id BIGINT NOT NULL,
    cnpj VARCHAR(18) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    state_registration VARCHAR(50) NULL,
    CONSTRAINT pk_legal_entities PRIMARY KEY (user_id),
    CONSTRAINT fk_legal_entities_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uk_legal_entities_cnpj UNIQUE (cnpj)
);
CREATE INDEX idx_legal_entities_cnpj ON legal_entities(cnpj);
-- 4. Tabela de refresh tokens
CREATE TABLE IF NOT EXISTS refresh_tokens (
    refresh_token_id BIGSERIAL,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (refresh_token_id),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uk_refresh_tokens_token UNIQUE (token),
    CONSTRAINT uk_refresh_tokens_user UNIQUE (user_id)
);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
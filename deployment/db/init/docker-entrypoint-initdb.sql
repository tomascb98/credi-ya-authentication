-- Crea esquema para microservicio de autenticación
-- En el futuro: payments, users, products, etc.
CREATE SCHEMA IF NOT EXISTS auth;

-- Habilita la extensión UUID para generar UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS auth.roles (
                                          id SERIAL PRIMARY KEY,
                                          nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
    );

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS auth.users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    document_number VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    salary_base DECIMAL(15,2),
    user_role_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (user_role_id) REFERENCES auth.roles(id)
);

-- Datos de ejemplo para roles
INSERT INTO auth.roles (nombre, descripcion)
VALUES ('ADMIN', 'Rol administrador'),
       ('USER', 'Usuario regular'),
       ('MANAGER', 'Gerente')
    ON CONFLICT (nombre) DO NOTHING;

-- Datos de ejemplo para usuarios
INSERT INTO auth.users (name, email, password, document_number, phone_number, salary_base, user_role_id)
VALUES ('Admin User', 'admin@crediya.com', '$2a$10$example.hash.password', '12345678', '+57300123456', 5000000.00, 1),
       ('Test User', 'user@crediya.com', '$2a$10$example.hash.password', '87654321', '+57300654321', 2500000.00, 2)
    ON CONFLICT (email) DO NOTHING;

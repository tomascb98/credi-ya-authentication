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
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    address TEXT NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(255) NOT NULL UNIQUE,
    salary_base DECIMAL(15,2) NOT NULL CHECK (salary_base >= 0 AND salary_base <= 15000000),
    password VARCHAR(255) NOT NULL,
    document_number VARCHAR(50) NOT NULL UNIQUE,
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
INSERT INTO auth.users (name, last_name, birth_date, address, phone_number, email, salary_base, password, document_number, user_role_id)
VALUES ('Admin', 'User', '1990-01-15', 'Calle 123 #45-67, Bogotá', '+57300123456', 'admin@crediya.com', 5000000.00, '$2a$10$example.hash.password', '12345678', 1),
       ('Test', 'User', '1985-06-20', 'Carrera 89 #12-34, Medellín', '+57300654321', 'user@crediya.com', 2500000.00, '$2a$10$example.hash.password', '87654321', 2)
    ON CONFLICT (email) DO NOTHING;

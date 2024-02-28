-- liquibase formatted sql

-- changeset argus:1709165800853-1
ALTER TABLE customer
    ALTER COLUMN account_id DROP NOT NULL;


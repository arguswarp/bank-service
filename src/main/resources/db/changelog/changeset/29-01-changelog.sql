-- liquibase formatted sql

-- changeset argus:1709165692145-1
ALTER TABLE customer
    ALTER COLUMN account_id DROP NOT NULL;


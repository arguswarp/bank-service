-- liquibase formatted sql

-- changeset argus:1709247493018-1
ALTER TABLE account
    ADD balance DECIMAL;

-- changeset argus:1709247493018-2
ALTER TABLE account
    ALTER COLUMN balance SET NOT NULL;


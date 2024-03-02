-- liquibase formatted sql

-- changeset argus:1709336915484-1
ALTER TABLE customer
    DROP CONSTRAINT uc_customer_password;


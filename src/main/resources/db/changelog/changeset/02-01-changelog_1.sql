-- liquibase formatted sql

-- changeset argus:1709336687572-1
ALTER TABLE customer
    DROP CONSTRAINT uc_customer_date_of_birth;


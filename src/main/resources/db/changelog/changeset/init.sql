-- liquibase formatted sql

-- changeset argus:1709161638109-1
CREATE TABLE account (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, deposit DECIMAL NOT NULL, CONSTRAINT pk_account PRIMARY KEY (id));

-- changeset argus:1709161638109-2
CREATE TABLE contact (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, phone VARCHAR(255), email VARCHAR(255), customer_id BIGINT, CONSTRAINT pk_contact PRIMARY KEY (id));

-- changeset argus:1709161638109-3
CREATE TABLE customer (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL, full_name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, phone VARCHAR(255) NOT NULL, date_of_birth date NOT NULL, account_id BIGINT NOT NULL, CONSTRAINT pk_customer PRIMARY KEY (id));

-- changeset argus:1709161638109-4
ALTER TABLE contact ADD CONSTRAINT uc_contact_email UNIQUE (email);

-- changeset argus:1709161638109-5
ALTER TABLE contact ADD CONSTRAINT uc_contact_phone UNIQUE (phone);

-- changeset argus:1709161638109-6
ALTER TABLE customer ADD CONSTRAINT uc_customer_date_of_birth UNIQUE (date_of_birth);

-- changeset argus:1709161638109-7
ALTER TABLE customer ADD CONSTRAINT uc_customer_email UNIQUE (email);

-- changeset argus:1709161638109-8
ALTER TABLE customer ADD CONSTRAINT uc_customer_password UNIQUE (password);

-- changeset argus:1709161638109-9
ALTER TABLE customer ADD CONSTRAINT uc_customer_phone UNIQUE (phone);

-- changeset argus:1709161638109-10
ALTER TABLE customer ADD CONSTRAINT uc_customer_username UNIQUE (username);

-- changeset argus:1709161638109-11
ALTER TABLE contact ADD CONSTRAINT FK_CONTACT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

-- changeset argus:1709161638109-12
ALTER TABLE customer ADD CONSTRAINT FK_CUSTOMER_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);


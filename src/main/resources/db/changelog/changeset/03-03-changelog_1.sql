-- liquibase formatted sql

-- changeset argus: 1
alter table public.customer
    drop constraint fk_customer_on_account;

alter table public.customer
    add constraint fk_customer_on_account
        foreign key (account_id) references public.account
            on delete cascade;


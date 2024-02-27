package com.argus.bankservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //TODO: mb use BigDecimal?
    @PositiveOrZero
    private Double deposit;
    @OneToOne
    private Customer owner;
}

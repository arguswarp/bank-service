package com.argus.bankservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;
    /**
     * ФИО
     */
    private String fullName;
    //TODO: better validation
    @PastOrPresent
    private LocalDate dateOfBirth;
    @NotNull
    @OneToMany
    private List<Contact> contacts;
    @NotNull
    @OneToOne
    private Account account;
}

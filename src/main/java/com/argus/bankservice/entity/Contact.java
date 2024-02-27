package com.argus.bankservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //TODO: add pattern for phone number
    @Pattern(regexp = "")
    private String phone;
    @Email
    private String email;
    @ManyToOne
    private Customer owner;
}

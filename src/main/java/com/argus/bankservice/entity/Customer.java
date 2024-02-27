package com.argus.bankservice.entity;

import com.argus.bankservice.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
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

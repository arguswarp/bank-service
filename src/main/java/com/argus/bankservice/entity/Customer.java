package com.argus.bankservice.entity;

import com.argus.bankservice.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
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

    private String email;

    private String phone;

    private LocalDate dateOfBirth;
    /**
     * дополнительные контакты
     */
    @NotNull
    @OneToMany
    private List<Contact> contacts;
    @NotNull
    @OneToOne
    private Account account;
}

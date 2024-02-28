package com.argus.bankservice.entity;

import com.argus.bankservice.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", unique = true, nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    /**
     * ФИО
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;
    @Column(name = "date_of_birth", unique = true, nullable = false)
    private LocalDate dateOfBirth;
    /**
     * дополнительные контакты
     */
    @OneToMany(mappedBy = "owner")
    private List<Contact> contacts;

    @OneToOne
    private Account account;
}

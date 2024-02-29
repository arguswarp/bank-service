package com.argus.bankservice.entity;

import com.argus.bankservice.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@DynamicUpdate
@Table(name = "customer")
public class Customer {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @JsonIgnore()
    @Column(name = "password", unique = true, nullable = false)
    private String password;
    @JsonIgnore
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    /**
     * ФИО
     */
    @JsonIgnore
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;
    @JsonIgnore
    @Column(name = "date_of_birth", unique = true, nullable = false)
    private LocalDate dateOfBirth;
    /**
     * дополнительные контакты
     */
    @OneToMany(mappedBy = "owner")
    private List<Contact> contacts;
    @JsonIgnore
    @OneToOne
    private Account account;
}

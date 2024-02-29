package com.argus.bankservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "account")
public class Account {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "deposit", nullable = false)
    private BigDecimal deposit;

    @Column(name = "deposit", nullable = false)
    private BigDecimal balance;
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "account")
    private Customer owner;
}

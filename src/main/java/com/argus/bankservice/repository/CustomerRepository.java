package com.argus.bankservice.repository;

import com.argus.bankservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> existsByUsername(String username);
}

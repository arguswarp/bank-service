package com.argus.bankservice.repository;

import com.argus.bankservice.entity.Contact;
import com.argus.bankservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    @EntityGraph(attributePaths = {"contacts"})
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByContactsIsContaining(Contact contact);

    Page<Customer> findAllByFullNameContaining(String fullname, Pageable pageable);

    Page<Customer> findAllByDateOfBirthGreaterThan(LocalDate date, Pageable pageable);
}

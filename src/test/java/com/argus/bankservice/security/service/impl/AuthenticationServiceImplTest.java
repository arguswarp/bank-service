package com.argus.bankservice.security.service.impl;

import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.Account;
import com.argus.bankservice.exception.CustomerAlreadyExistException;
import com.argus.bankservice.repository.AccountRepository;
import com.argus.bankservice.repository.ContactRepository;
import com.argus.bankservice.repository.CustomerRepository;
import com.argus.bankservice.security.service.AuthenticationService;
import com.argus.bankservice.service.PostgresTestWithContainers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
class AuthenticationServiceImplTest extends PostgresTestWithContainers {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ContactRepository contactRepository;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void prepareDB() {

        contactRepository.deleteAll();
        customerRepository.deleteAll();
        accountRepository.deleteAll();

        signUpRequest = SignUpRequest.builder()
                .username("Darth Vader")
                .password("deathstar333")
                .email("any@darkside.com")
                .phone("+79990001138")
                .fullName("Anakin Skywalker")
                .dateOfBirth(LocalDate.of(1968, 1, 1))
                .deposit(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void WhenSignUp_UserIsCreated() {
        var response = authenticationService.signUp(signUpRequest);
        Assertions.assertNotNull(response);
        var customerPersistent = customerRepository.findByUsername(signUpRequest.getUsername());
        Assertions.assertTrue(customerPersistent.isPresent());
        Assertions.assertNotNull(customerPersistent.get().getAccount());
    }

    @Test
    void WhenSignUpTwice_ExceptionThrownAndAnotherAccountIsNotCreated() {
        authenticationService.signUp(signUpRequest);
        Assertions.assertThrows(CustomerAlreadyExistException.class, () -> authenticationService.signUp(signUpRequest));
        List<Account> accounts = accountRepository.findAll().stream().filter(account -> !account.getOwner().getUsername().equals(signUpRequest.getUsername())).toList();
        Assertions.assertTrue(accounts.isEmpty());
    }
}
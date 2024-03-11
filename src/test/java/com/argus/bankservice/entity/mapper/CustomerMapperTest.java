package com.argus.bankservice.entity.mapper;

import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {
    @InjectMocks
    private final CustomerMapper customerMapper = new CustomerMapperImpl();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private PasswordEncoder mockEncoder;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void prepareEntity() {
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
    void signUpRequestToCustomer() {
        Mockito.when(mockEncoder.encode(signUpRequest.getPassword())).thenReturn(passwordEncoder.encode(signUpRequest.getPassword()));
        var customer = customerMapper.signUpRequestToCustomer(signUpRequest);
        Assertions.assertNotNull(customer);
        Assertions.assertNotNull(customer.getAccount());
        Assertions.assertEquals(signUpRequest.getUsername(), customer.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(signUpRequest.getPassword(), customer.getPassword()));
        Assertions.assertEquals(signUpRequest.getDateOfBirth(), customer.getDateOfBirth());
        Assertions.assertEquals(signUpRequest.getDeposit(), customer.getAccount().getDeposit());
        Assertions.assertEquals(signUpRequest.getDeposit(), customer.getAccount().getBalance());
        Assertions.assertEquals(Role.ROLE_USER, customer.getRole());
    }
}
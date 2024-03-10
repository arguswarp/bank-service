package com.argus.bankservice.controller;

import com.argus.bankservice.dto.JwtAuthenticationResponse;
import com.argus.bankservice.dto.SignInRequest;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.entity.enums.Role;
import com.argus.bankservice.repository.AccountRepository;
import com.argus.bankservice.repository.ContactRepository;
import com.argus.bankservice.repository.CustomerRepository;
import com.argus.bankservice.security.service.JWTService;
import com.argus.bankservice.service.CustomerService;
import com.argus.bankservice.service.PostgresTestWithContainers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
class AuthControllerTest extends PostgresTestWithContainers {
    private static final String BASE_URI = "http://localhost:";
    private static final String SIGN_UP_URI = "/auth/sign-up";
    private static final String SIGN_IN_URI = "/auth/sign-in";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private Map<String, String> request;
    private JwtAuthenticationResponse token;

    @BeforeEach
    void prepareDB() {
        contactRepository.deleteAll();
        customerRepository.deleteAll();
        accountRepository.deleteAll();

        token = null;
        request = new HashMap<>();
        request.put("username", "DarthVader");
        request.put("password", "deathstar333");
        request.put("email", "any@darkside.com");
        request.put("phone", "+79990001138");
        request.put("fullName", "Anakin Skywalker");
        request.put("dateOfBirth", "25.05.1977");
        request.put("deposit", "1000");
    }

    @Test
    @Transactional
    void WhenSignUp_ThenCode200() {
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = testRestTemplate.postForEntity(BASE_URI + port + SIGN_UP_URI,
                request,
                JwtAuthenticationResponse.class);
        token = signUpResponse.getBody();
        Assertions.assertNotNull(token);
        Assertions.assertTrue(jwtService.isValidToken(token.getToken(), customerService.userDetailsService().loadUserByUsername("DarthVader")));
        Assertions.assertEquals(HttpStatus.OK, signUpResponse.getStatusCode());
    }

    @Test
    @Transactional
    void WhenSignIn_ThenCode200() {
        customerRepository.save(Customer.builder()
                .username("DarthVader")
                .password(passwordEncoder.encode("deathstar333"))
                .email("any@darkside.com")
                .phone("+79990001138")
                .fullName("Anakin Skywalker")
                .dateOfBirth(LocalDate.of(1968, 1, 1))
                .role(Role.ROLE_USER)
                .build());

        ResponseEntity<JwtAuthenticationResponse> signInResponse = testRestTemplate.postForEntity(BASE_URI + port + SIGN_IN_URI,
                SignInRequest.builder()
                        .username("DarthVader")
                        .password("deathstar333")
                        .build(), JwtAuthenticationResponse.class);
        token = signInResponse.getBody();
        Assertions.assertNotNull(token);
        Assertions.assertTrue(jwtService.isValidToken(token.getToken(), customerService.userDetailsService().loadUserByUsername("DarthVader")));
        Assertions.assertEquals(HttpStatus.OK, signInResponse.getStatusCode());
    }

    @Test
    @Transactional
    void WhenSignUpWithBadData_ThenBadRequest() {
        request.put("username", "a");
        request.put("email", "1");
        ResponseEntity<Object> signUpResponse = testRestTemplate.postForEntity(BASE_URI + port + SIGN_UP_URI,
                request,
                Object.class);
        Assertions.assertNotNull(signUpResponse.getBody());
        log.info(Objects.requireNonNull(signUpResponse.getBody()).toString());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, signUpResponse.getStatusCode());
    }
}
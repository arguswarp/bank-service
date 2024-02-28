package com.argus.bankservice.security.service.impl;

import com.argus.bankservice.dto.JwtAuthenticationResponse;
import com.argus.bankservice.dto.SignInRequest;
import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.security.CustomerDetails;
import com.argus.bankservice.security.service.AuthenticationService;
import com.argus.bankservice.security.service.JWTService;
import com.argus.bankservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerService customerService;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    /**
     * Регистрация пользователя
     *
     * @param signUpRequest данные для регистрации
     * @return jwt токен
     */
    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest) {
        var customer = modelMapper.map(signUpRequest, Customer.class);
        customerService.create(customer);
        var token = jwtService.generateToken(new CustomerDetails(customer));
        return new JwtAuthenticationResponse(token);
    }

    /**
     * Аутентификация пользователя
     *
     * @param signInRequest данные для входа
     * @return jwt токен
     */
    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),
                signInRequest.getPassword()
        ));
        var customer = customerService
                .userDetailsService()
                .loadUserByUsername(signInRequest.getUsername());
        var token = jwtService.generateToken(customer);
        return new JwtAuthenticationResponse(token);
    }
}

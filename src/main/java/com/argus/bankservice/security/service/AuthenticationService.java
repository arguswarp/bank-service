package com.argus.bankservice.security.service;

import com.argus.bankservice.dto.JwtAuthenticationResponse;
import com.argus.bankservice.dto.SignInRequest;
import com.argus.bankservice.dto.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);

}

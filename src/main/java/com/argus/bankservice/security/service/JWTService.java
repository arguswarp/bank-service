package com.argus.bankservice.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);
}

package com.argus.bankservice.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);
}

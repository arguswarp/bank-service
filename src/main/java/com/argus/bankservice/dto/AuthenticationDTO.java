package com.argus.bankservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthenticationDTO {
    @NotEmpty
    @Size(min = 2, max = 100, message = "Name should not be empty")
    private String login;

    private String password;
}

package com.argus.bankservice.dto;

import jakarta.validation.constraints.*;

public class UserDTO {
    @NotEmpty
    @Size(min = 2, max = 100, message = "Name should not be empty")
    private String login;
    @NotEmpty
    @Min(value = 8, message = "Password must be at least 8 symbols long")
    private String password;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String phone;
    @Positive
    private Double deposit;
}

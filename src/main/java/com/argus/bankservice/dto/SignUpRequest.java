package com.argus.bankservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SignUpRequest {
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String login;
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @Size(min = 5, max = 50, message = "ФИО должно содержать от 5 до 100 символов")
    @NotBlank
    private String fullName;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    private LocalDate dateOfBirth;
    @Positive
    private BigDecimal deposit;
}

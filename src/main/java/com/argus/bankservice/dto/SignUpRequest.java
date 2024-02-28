package com.argus.bankservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
    @NotBlank
    @Email
    private String email;
    /**
     * Телефон в международном формате, например +70001234567
     */
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{10}$")
    private String phone;
    @Size(min = 5, max = 50, message = "ФИО должно содержать от 5 до 100 символов")
    @NotBlank
    private String fullName;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    @Positive
    private BigDecimal deposit;
}

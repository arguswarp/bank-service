package com.argus.bankservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactUpdateDTO extends ContactDTO {
    @Pattern(regexp = "^\\+[1-9]\\d{10}$", message = "Номер должен соответствовать международному формату, например +79991234567")
    private String oldPhone;
    @Email
    private String oldEmail;
}

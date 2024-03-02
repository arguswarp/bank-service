package com.argus.bankservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangeBalanceRequest {
    @Positive(message = "Сумма должна быть больше 0")
    @NotNull(message = "Значение не может быть пустым")
    private BigDecimal amount;
}

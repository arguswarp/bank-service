package com.argus.bankservice.dto;

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
    private BigDecimal amount;
}

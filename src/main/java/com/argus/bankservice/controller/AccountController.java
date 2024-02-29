package com.argus.bankservice.controller;

import com.argus.bankservice.dto.ChangeBalanceRequest;
import com.argus.bankservice.service.AccountService;
import com.argus.bankservice.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<?> showBalance(Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        var account = accountService.findByOwner(customer);
        return ResponseEntity.ok(account.getBalance());
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@Valid @RequestBody ChangeBalanceRequest withdrawRequest, Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        var account = accountService.withdraw(withdrawRequest.getAmount(), accountService.findByOwner(customer));
        log.info(String.format("Пользователь: %s вывел средства в кол-ве: %.2f, текущий баланс: %.2f",
                customer.getUsername(),
                withdrawRequest.getAmount(),
                account.getBalance()
        ));
        return ResponseEntity.ok().build();
    }
}

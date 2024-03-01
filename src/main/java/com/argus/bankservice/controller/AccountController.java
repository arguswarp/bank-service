package com.argus.bankservice.controller;

import com.argus.bankservice.dto.ChangeBalanceRequest;
import com.argus.bankservice.service.AccountService;
import com.argus.bankservice.service.CustomerService;
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

    private final CustomerService customerService;

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
        return ResponseEntity.ok(account.getBalance());
    }

    @PatchMapping("/replenish")
    public ResponseEntity<?> replenishMoney(@Valid @RequestBody ChangeBalanceRequest withdrawRequest, Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        var account = accountService.replenish(withdrawRequest.getAmount(), accountService.findByOwner(customer));
        log.info(String.format("Пользователь: %s пополнил баланс на сумму: %.2f, текущий баланс: %.2f",
                customer.getUsername(),
                withdrawRequest.getAmount(),
                account.getBalance()
        ));
        return ResponseEntity.ok(account.getBalance());
    }

    @PatchMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody ChangeBalanceRequest withdrawRequest,
                                           @RequestParam Long id,
                                           Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        var accountFrom = accountService.findByOwner(customer);
        var customerTo = customerService.findById(id);
        var accountTo = customerTo.getAccount();

        accountService.transfer(withdrawRequest.getAmount(), accountFrom, accountTo);
        log.info(String.format("Пользователь 1: %s перевел средства пользователю 2: %s, в кол-ве: %.2f, текущий баланс 1: %.2f, текущий баланс 2: %.2f",
                customer.getUsername(),
                customerTo.getUsername(),
                withdrawRequest.getAmount(),
                accountFrom.getBalance(),
                accountTo.getBalance()
        ));
        return ResponseEntity.ok(accountFrom.getBalance());
    }
}

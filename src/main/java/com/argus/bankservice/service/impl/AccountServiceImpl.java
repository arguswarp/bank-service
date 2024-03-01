package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.exception.AccountNotFoundException;
import com.argus.bankservice.exception.NegativeBalanceException;
import com.argus.bankservice.repository.AccountRepository;
import com.argus.bankservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account withdraw(BigDecimal amount, Account account) {
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.subtract(amount, MathContext.DECIMAL32));
        if (isBalanceNegative(account.getBalance())) {
            throw new NegativeBalanceException("Баланс не может быть отрицательным");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account replenish(BigDecimal amount, Account account) {
        account.setBalance(account.getBalance().add(amount, MathContext.DECIMAL32));
        return accountRepository.save(account);
    }

    @Override
    public void transfer(BigDecimal amount, Account from, Account to) {
        BigDecimal balance = from.getBalance();
        from.setBalance(balance.subtract(amount, MathContext.DECIMAL32));
        if (isBalanceNegative(from.getBalance())) {
            throw new NegativeBalanceException("Баланс не может быть отрицательным");
        }
        to.setBalance(to.getBalance().add(amount, MathContext.DECIMAL32));
        accountRepository.saveAll(List.of(from,to));
    }

    @Override
    public Account findByOwner(Customer owner) {
        return accountRepository.findByOwner(owner).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
    }

    private Boolean isBalanceNegative(BigDecimal balance) {
        return balance.signum() < 0;
    }
}

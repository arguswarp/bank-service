package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.exception.AccountNotFoundException;
import com.argus.bankservice.exception.DatabaseLockException;
import com.argus.bankservice.exception.OverdraftException;
import com.argus.bankservice.repository.AccountRepository;
import com.argus.bankservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final Double MULTIPLY_FACTOR_DEPOSIT = 2.07;
    private static final Double MULTIPLY_FACTOR_INTEREST = 1.05;
    private final AccountRepository accountRepository;

    @Override
    public Account withdraw(BigDecimal amount, Customer customer) {
        var account = accountRepository.findByOwner(customer).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.subtract(amount, MathContext.DECIMAL32));
        if (isBalanceNegative(account.getBalance())) {
            throw new OverdraftException("На счете недостаточно средств");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account replenish(BigDecimal amount, Customer customer) {
        var account = accountRepository.findByOwner(customer).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
        account.setBalance(account.getBalance().add(amount, MathContext.DECIMAL32));
        return accountRepository.save(account);
    }

    @Override
    public void transfer(BigDecimal amount, Customer from, Customer to) {
        try {
            var accountFrom = accountRepository.findByOwner(from).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
            var accountTo = accountRepository.findByOwner(to).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));

            var balanceFrom = accountFrom.getBalance();
            balanceFrom = balanceFrom.subtract(amount);
            accountRepository.updateBalanceByOwner(balanceFrom, from);

            if (isBalanceNegative(balanceFrom)) {
                throw new OverdraftException("На счете недостаточно средств, чтобы выполнить перевод");
            }

            var balanceTO = accountTo.getBalance().add(amount);
            accountRepository.updateBalanceByOwner(balanceTO, to);

            log.info(String.format("Пользователь 1: %s перевел средства пользователю 2: %s, в кол-ве: %.2f, текущий баланс 1: %.2f, текущий баланс 2: %.2f",
                    from.getUsername(),
                    to.getUsername(),
                    amount,
                    balanceFrom,
                    balanceTO
            ));
        } catch (CannotAcquireLockException e) {
            throw new DatabaseLockException("Ошибка перевода. Попробуйте выполнить операцию заново");
        }
    }

    @Scheduled(fixedRate = 60_000, initialDelay = 60_000)
    public void interestJob() {
        log.info("Проверяю можно ли начислить проценты");
        List<Account> accounts = accountRepository.findAll();
        List<Account> accountsForInterest = accounts.stream()
                .filter(account -> account.getBalance()
                        .multiply(BigDecimal.valueOf(MULTIPLY_FACTOR_INTEREST))
                        .compareTo(account.getDeposit()
                                .multiply(BigDecimal.valueOf(MULTIPLY_FACTOR_DEPOSIT))) < 1).toList();
        if (accountsForInterest.isEmpty()) {
            log.info("Начислять некому");
            return;
        }
        accountsForInterest.forEach(account -> accountRepository.updateBalanceByOwner(
                        account.getBalance().multiply(BigDecimal.valueOf(MULTIPLY_FACTOR_INTEREST)),
                        account.getOwner()
                )
        );
        log.info("Проценты начислены для:\n" + accountsForInterest.stream().map(account -> account.getOwner().getUsername()).collect(Collectors.joining("\n")));
    }

    @Override
    public Account findByOwner(Customer owner) {
        return accountRepository.findByOwner(owner).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
    }

    @Override
    public void updateBalance(BigDecimal amount, Customer customer) {
        accountRepository.updateBalanceByOwner(amount, customer);
    }

    private Boolean isBalanceNegative(BigDecimal balance) {
        return balance.signum() < 0;
    }
}

package com.argus.bankservice.service;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface AccountService {

    Account withdraw(BigDecimal amount, Customer customer);

    Account replenish(BigDecimal amount, Customer customer);

    /**
     * Перевод денег с аккаунта на аккаунт
     *
     * @param amount сколько денег нужно перевести
     * @param from   с какого аккаунта
     * @param to     на какой аккаунт
     */
    void transfer(BigDecimal amount, Customer from, Customer to);

    Account findByOwner(Customer owner);

    void updateBalance(BigDecimal amount, Customer customer);

    Account createAccount(Account account);
}

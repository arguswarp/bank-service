package com.argus.bankservice.service;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public interface AccountService {

    Account withdraw(BigDecimal amount, Account account);

    Account replenish(BigDecimal amount, Account account);

    /**
     * Перевод денег с аккаунта на аккаунт
     *
     * @param amount сколько денег нужно перевести
     * @param from   с какого аккаунта
     * @param to     на какой аккаунт
     */
    void transfer(BigDecimal amount, Account from, Account to);

    Account findByOwner(Customer owner);
}

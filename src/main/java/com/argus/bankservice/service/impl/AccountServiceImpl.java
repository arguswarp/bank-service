package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.service.AccountService;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {
    @Override
    public Account withdraw(BigDecimal amount, Account account) {
        return null;
    }

    @Override
    public Account replenish(BigDecimal amount, Account account) {
        return null;
    }

    @Override
    public Account transfer(BigDecimal amount, Account from, Account to) {
        return null;
    }
}

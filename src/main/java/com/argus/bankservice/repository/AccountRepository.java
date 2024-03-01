package com.argus.bankservice.repository;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query("update Account a set a.balance = ?1 where a.owner = ?2")
    void updateBalanceByOwner(BigDecimal amount, Customer owner);

    Optional<Account> findByOwner(Customer owner);

}

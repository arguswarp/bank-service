package com.argus.bankservice.repository;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByOwner(Customer owner);
}

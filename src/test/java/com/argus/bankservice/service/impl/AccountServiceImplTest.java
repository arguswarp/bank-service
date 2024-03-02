package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.entity.enums.Role;
import com.argus.bankservice.exception.OverdraftException;
import com.argus.bankservice.repository.AccountRepository;
import com.argus.bankservice.repository.ContactRepository;
import com.argus.bankservice.repository.CustomerRepository;
import com.argus.bankservice.service.AccountService;
import com.argus.bankservice.service.CustomerService;
import com.argus.bankservice.service.PostgresTestWithContainers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
class AccountServiceImplTest extends PostgresTestWithContainers {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ContactRepository contactRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    Customer customer1;
    Customer customer2;
    Customer customer3;

    @BeforeEach
    void prepareDB() {
        contactRepository.deleteAll();
        customerRepository.deleteAll();
        accountRepository.deleteAll();

        var account1 = accountService.createAccount(Account.builder()
                .deposit(BigDecimal.valueOf(1000))
                .balance(BigDecimal.valueOf(1000))
                .owner(customer1)
                .build()
        );
        var account2 = accountService.createAccount(Account.builder()
                .deposit(BigDecimal.valueOf(1000))
                .balance(BigDecimal.valueOf(1000))
                .owner(customer2)
                .build()
        );
        var account3 = accountService.createAccount(Account.builder()
                .deposit(BigDecimal.valueOf(1000))
                .balance(BigDecimal.valueOf(1000))
                .owner(customer3)
                .build()
        );
        customer1 = customerService.create(Customer.builder()
                .username("user1")
                .email("1@mail.ru")
                .role(Role.ROLE_USER)
                .phone("+79991234567")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .fullName("Ivanov Ivan Ivanych")
                .password("123456789")
                .account(account1)
                .build());
        customer2 = customerService.create(Customer.builder()
                .username("user2")
                .email("2@mail.ru")
                .role(Role.ROLE_USER)
                .phone("+79891234567")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .fullName("Porfiry")
                .password("123456789")
                .account(account2)
                .build());
        customer3 = customerService.create(Customer.builder()
                .username("user3")
                .email("3@mail.ru")
                .role(Role.ROLE_USER)
                .phone("+79791234567")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .fullName("Batman")
                .password("123456789")
                .account(account3)
                .build());
    }

    @Test
    void WhenTransferCompleted_ThenOverallBalanceIsSame() {

        log.info(String.format("Balance before, max: %.02f, Ann: %.02f", customer1.getAccount().getBalance(), customer2.getAccount().getBalance()));

        AtomicInteger count = new AtomicInteger(0);
        // Create a list of CompletableFuture objects using a stream
        List<CompletableFuture<Void>> futures = IntStream.range(0, 500)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        accountService.transfer(BigDecimal.ONE, customer1, customer2);
                        Thread.sleep(1000);
                        accountService.transfer(BigDecimal.ONE, customer2, customer3);
                        Thread.sleep(1000);
                        count.incrementAndGet();
                    } catch (RuntimeException e) {
                        log.error("Сработала блокировка. {}", e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toList();

        // Wait for all CompletableFuture objects to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        executorService.shutdown();

        customer1 = customerService.findByUsername("user1");
        customer2 = customerService.findByUsername("user2");
        customer3 = customerService.findByUsername("user3");

        int balance1 = accountService.findByOwner(customer1).getBalance().intValue();
        int balance2 = accountService.findByOwner(customer2).getBalance().intValue();
        int balance3 = accountService.findByOwner(customer3).getBalance().intValue();

        log.info(String.format("Balance after, customer 1: %d, customer 2: %d, customer 3: %d", balance1, balance2, balance3));
        log.info("Успешных переводов из 500 всего: " + count);
        Assertions.assertEquals(3000, balance1 + balance2 + balance3);
    }

    @Test
    void WhenTransferedTooMuch_ThenOverdraftExceptionThrown() {
        Assertions.assertThrows(OverdraftException.class,
                () -> accountService.transfer(BigDecimal.valueOf(1500), customer1, customer2));
    }
}
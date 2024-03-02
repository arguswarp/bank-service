package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Account;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.entity.enums.Role;
import com.argus.bankservice.exception.CustomerAlreadyExistException;
import com.argus.bankservice.exception.OverdraftException;
import com.argus.bankservice.service.AccountService;
import com.argus.bankservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    Customer customer1;
    Customer customer2;
    Customer customer3;

    @BeforeEach
    void prepare() {
        try {
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
                    .id(1L)
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
                    .id(2L)
                    .username("user2")
                    .email("2@mail.ru")
                    .role(Role.ROLE_USER)
                    .phone("+79891234567")
                    .dateOfBirth(LocalDate.of(2000, 1, 1))
                    .fullName("Porfiry")
                    .password("123456789")
                    .account(account2)
                    .build());
            customer2 = customerService.create(Customer.builder()
                    .id(3L)
                    .username("user3")
                    .email("3@mail.ru")
                    .role(Role.ROLE_USER)
                    .phone("+79791234567")
                    .dateOfBirth(LocalDate.of(2000, 1, 1))
                    .fullName("Batman")
                    .password("123456789")
                    .account(account3)
                    .build());
        } catch (CustomerAlreadyExistException e) {
            log.info("Юзеры уже есть");
        } finally {
            customer1 = customerService.findById(1L);
            customer2 = customerService.findById(2L);
            customer3 = customerService.findById(3L);

            accountService.updateBalance(BigDecimal.valueOf(1000), customer1);
            accountService.updateBalance(BigDecimal.valueOf(1000), customer2);
            accountService.updateBalance(BigDecimal.valueOf(1000), customer3);
        }
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

        Customer customer11 = customerService.findById(1L);
        Customer customer22 = customerService.findById(2L);
        Customer customer33 = customerService.findById(3L);

        int balance1 = customer11.getAccount().getBalance().intValue();
        int balance2 = customer22.getAccount().getBalance().intValue();
        int balance3 = customer33.getAccount().getBalance().intValue();

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
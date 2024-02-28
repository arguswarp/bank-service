package com.argus.bankservice.service;

import com.argus.bankservice.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer create(Customer customer);

    Customer save(Customer customer);

    Customer getByUsername(String username);

    UserDetailsService userDetailsService();

    /**
     * Получение пользователя из контекста Spring Security
     *
     * @return Customer
     */
    Customer getCurrentCustomer();

    Boolean updatePhone(String phone, Customer customer);

    Boolean addPhone(String phone, Customer customer);

    Boolean updateEmail(String email, Customer customer);

    Boolean addEmail(String email, Customer customer);

    Boolean addEmailAndPhone(String email, String phone, Customer customer);
}

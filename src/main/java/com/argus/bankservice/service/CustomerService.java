package com.argus.bankservice.service;

import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.Contact;
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
     * @return Customer
     */
    Customer getCurrentCustomer();

    Boolean updatePhone(String phone, Long id, Contact contact, Customer customer);

    Boolean addPhone(String phone,Long id, Contact contact, Customer customer);

    Boolean updateEmail(String email, Long id,Contact contact, Customer customer);

    Boolean addEmail(String email,Long id, Contact contact, Customer customer);
}

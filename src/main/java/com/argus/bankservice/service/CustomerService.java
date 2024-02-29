package com.argus.bankservice.service;

import com.argus.bankservice.dto.ContactDTO;
import com.argus.bankservice.dto.ContactUpdateDTO;
import com.argus.bankservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface CustomerService {

    UserDetailsService userDetailsService();

    Customer create(Customer customer);

    Customer save(Customer customer);

    Customer findByUsername(String username);

    Customer findByPhone(String phone);

    Customer findByEmail(String email);

    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findAllByDateOfBirth(LocalDate date, Pageable pageable);

    Page<Customer> findAllByFullName(String fullName, Pageable pageable);

    void addContact(ContactDTO contactDTO, Customer customer);

    void updateContact(ContactUpdateDTO contactUpdateDTO, Customer customer);

    void updateAdditionalContact(ContactUpdateDTO contactUpdateDTO, Customer customer);

    void deletePhone(String phone, Customer customer);

    void deleteEmail(String email, Customer customer);
}

package com.argus.bankservice.service.impl;

import com.argus.bankservice.entity.Contact;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.exception.CustomerAlreadyExistException;
import com.argus.bankservice.exception.EmailAlreadyExistException;
import com.argus.bankservice.exception.PhoneAlreadyExistException;
import com.argus.bankservice.repository.ContactRepository;
import com.argus.bankservice.repository.CustomerRepository;
import com.argus.bankservice.security.CustomerDetails;
import com.argus.bankservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;

    @Override
    public Customer create(Customer customer) {
        if (customerRepository.existsByUsername(customer.getUsername())) {
            throw new CustomerAlreadyExistException("Пользователь с таким именем уже существует");
        }
        if (customerRepository.existsByEmail(customer.getEmail()) || contactRepository.existsByEmail(customer.getEmail())) {
            throw new EmailAlreadyExistException("Пользователь с таким email уже зарегистрирован");
        }
        if (customerRepository.existsByPhone(customer.getPhone()) || contactRepository.existsByPhone(customer.getPhone())) {
            throw new PhoneAlreadyExistException("Пользователь с таким номером телефона уже зарегистрирован");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer save(Customer customer) {
        return create(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getByUsername(String username) {
        return customerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> new CustomerDetails(getByUsername(username));
    }

    //TODO: mb remove
    @Override
    public Customer getCurrentCustomer() {
        var username = ((CustomerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return getByUsername(username);
    }

    @Override
    public Boolean updatePhone(String phone, Customer customer) {
        customer.setPhone(phone);
        save(customer);
        return true;
    }

    @Override
    public Boolean addPhone(String phone, Customer customer) {
        var newContact = Contact.builder().phone(phone).owner(customer).build();
        customer.getContacts().add(newContact);
        save(customer);
        contactRepository.save(newContact);
        return true;
    }

    @Override
    public Boolean updateEmail(String email, Customer customer) {
        customer.setEmail(email);
        save(customer);
        return true;
    }

    @Override
    public Boolean addEmail(String email, Customer customer) {
        var newContact = Contact.builder().email(email).owner(customer).build();
        customer.getContacts().add(newContact);
        save(customer);
        contactRepository.save(newContact);
        return true;
    }

    @Override
    public Boolean addEmailAndPhone(String email, String phone, Customer customer) {
        var newContact = Contact.builder().email(email).phone(phone).owner(customer).build();
        customer.getContacts().add(newContact);
        save(customer);
        contactRepository.save(newContact);
        return true;
    }
}

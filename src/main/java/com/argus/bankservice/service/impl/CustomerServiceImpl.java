package com.argus.bankservice.service.impl;

import com.argus.bankservice.dto.ContactDTO;
import com.argus.bankservice.dto.ContactUpdateDTO;
import com.argus.bankservice.entity.Contact;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.exception.ContactNotFoundException;
import com.argus.bankservice.exception.CustomerAlreadyExistException;
import com.argus.bankservice.exception.EmailAlreadyExistException;
import com.argus.bankservice.exception.PhoneAlreadyExistException;
import com.argus.bankservice.repository.ContactRepository;
import com.argus.bankservice.repository.CustomerRepository;
import com.argus.bankservice.security.CustomerDetails;
import com.argus.bankservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> new CustomerDetails(findByUsername(username));
    }

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
        log.info("Новый пользователь создан: " + customer.getUsername());
        return customerRepository.save(customer);
    }

    @Override
    public Customer save(Customer customer) {
        log.debug("Изменения пользователя сохранены: " + customer.getUsername());
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public Customer findByPhone(String phone) {
        var contact = contactRepository.getContactByPhone(phone);
        if (contact.isPresent()) {
            return customerRepository.findByContactsContaining(contact.get()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        }
        return customerRepository.findByPhone(phone).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public Customer findByEmail(String email) {
        var contact = contactRepository.getContactByEmail(email);
        if (contact.isPresent()) {
            return customerRepository.findByContactsContaining(contact.get()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        }
        return customerRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findAllByDateOfBirth(LocalDate date, Pageable pageable) {
        return customerRepository.findAllByDateOfBirthGreaterThan(date, pageable);
    }

    @Override
    public Page<Customer> findAllByFullName(String fullName, Pageable pageable) {
        return customerRepository.findAllByFullNameContaining(fullName, pageable);
    }

    @Override
    public void addContact(ContactDTO contactDTO, Customer customer) {
        var newContact = Contact.builder()
                .email(contactDTO.getEmail())
                .phone(contactDTO.getPhone())
                .owner(customer).build();
        log.debug("Пользователь " + customer.getUsername() + " добавил новый контакт " + contactDTO);
        contactRepository.save(newContact);
    }

    @Override
    public void updateContact(ContactUpdateDTO contactUpdateDTO, Customer customer) {
        if (contactUpdateDTO.getEmail() != null) {
            customer.setEmail(contactUpdateDTO.getEmail());
        }
        if (contactUpdateDTO.getPhone() != null) {
            customer.setPhone(contactUpdateDTO.getPhone());
        }
        log.debug("Пользователь " + customer.getUsername() + " изменил основной контакт " + contactUpdateDTO);
        save(customer);
    }

    @Override
    public void updateAdditionalContact(ContactUpdateDTO contactUpdateDTO, Customer customer) {
        var contactPersistent = contactRepository.getContactByEmailOrPhone(
                contactUpdateDTO.getOldEmail(),
                contactUpdateDTO.getOldPhone()
        ).orElseThrow(() -> new ContactNotFoundException("Контакт не найден"));
        contactPersistent.setEmail(contactUpdateDTO.getEmail());
        contactPersistent.setPhone(contactUpdateDTO.getPhone());
        log.debug("Пользователь " + customer.getUsername() + " изменил доп контакт " + contactUpdateDTO);
        contactRepository.save(contactPersistent);
    }

    @Override
    public void deletePhone(String phone, Customer customer) {
        contactRepository.deleteContactByPhone(phone);
    }

    @Override
    public void deleteEmail(String email, Customer customer) {
        contactRepository.deleteContactByEmail(email);
    }
}

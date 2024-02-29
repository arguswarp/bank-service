package com.argus.bankservice.repository;

import com.argus.bankservice.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    Optional<Contact> getContactByEmail(String email);

    Optional<Contact> getContactByPhone(String phone);

    Optional<Contact> getContactByEmailOrPhone(String email, String phone);

    void deleteContactByEmail(String email);

    void deleteContactByPhone(String phone);

}

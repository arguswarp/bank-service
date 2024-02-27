package com.argus.bankservice.service;

import com.argus.bankservice.dto.UserDTO;
import com.argus.bankservice.entity.Contact;
import com.argus.bankservice.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer addUser(UserDTO userDTO);

    Boolean updatePhone(String phone, Contact contact, Customer customer);

    Boolean addPhone(String phone, Contact contact, Customer customer);

    Boolean updateEmail(String email, Contact contact, Customer customer);

    Boolean addEmail(String email, Contact contact, Customer customer);
}

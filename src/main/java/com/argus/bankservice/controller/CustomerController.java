package com.argus.bankservice.controller;

import com.argus.bankservice.security.CustomerDetails;
import com.argus.bankservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/balance")
    public ResponseEntity<?> showBalance(Authentication authentication) {
        var customerDetails = (CustomerDetails) authentication.getPrincipal();
        var customer = customerDetails.getCustomer();
        return ResponseEntity.ok().body(customer.getAccount().getDeposit());
    }
}

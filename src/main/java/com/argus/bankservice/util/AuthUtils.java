package com.argus.bankservice.util;

import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.security.CustomerDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public static Customer getCustomer(Authentication authentication) {
        var customerDetails = (CustomerDetails) authentication.getPrincipal();
        return customerDetails.getCustomer();
    }
}

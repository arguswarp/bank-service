package com.argus.bankservice.entity.mapper;

import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.entity.enums.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(componentModel = "spring")
public abstract class CustomerMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "account.deposit", source = "deposit")
    @Mapping(target = "account.balance", source = "deposit")
    @Mapping(target = "password", ignore = true)
    public abstract Customer signUpRequestToCustomer(SignUpRequest signUpRequest);

    @AfterMapping
    protected void setRole(@MappingTarget Customer.CustomerBuilder builder) {
        builder.role(Role.ROLE_USER);
    }

    @AfterMapping
    protected void setPassword(@MappingTarget Customer.CustomerBuilder builder, SignUpRequest signUpRequest) {
        builder.password(passwordEncoder.encode(signUpRequest.getPassword()));
    }
}

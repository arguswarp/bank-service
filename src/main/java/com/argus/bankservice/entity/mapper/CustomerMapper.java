package com.argus.bankservice.entity.mapper;

import com.argus.bankservice.dto.SignUpRequest;
import com.argus.bankservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {

    @Mapping(target = "account.deposit", source = "deposit")
    @Mapping(target = "account.balance", source = "deposit")
    Customer signUpRequestToCustomer(SignUpRequest signUpRequest);
}

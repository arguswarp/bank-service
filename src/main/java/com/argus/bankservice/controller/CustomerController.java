package com.argus.bankservice.controller;

import com.argus.bankservice.dto.ContactDTO;
import com.argus.bankservice.dto.ContactUpdateDTO;
import com.argus.bankservice.entity.Customer;
import com.argus.bankservice.service.CustomerService;
import com.argus.bankservice.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/search")
    public ResponseEntity<?> getFiltered(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String fullname,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email
    ) {
        if (page != null && size != null) {
            List<Order> orders = new ArrayList<>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }
            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
            Page<Customer> pages;
            if (date != null) {
                pages = customerService.findAllByDateOfBirth(date, pageable);
            } else if (fullname != null) {
                pages = customerService.findAllByFullName(fullname, pageable);
            } else {
                pages = customerService.findAll(pageable);
            }
            List<Customer> customers = new ArrayList<>(pages.getContent());

            Map<String, Object> responseBody = Map.of(
                    "customers", customers,
                    "currentPage", pages.getNumber(),
                    "totalItems", pages.getTotalElements(),
                    "totalPages", pages.getTotalPages()
            );
            log.info(responseBody.toString());
            return ResponseEntity.ok(responseBody);
        }
        if (phone != null) {
            return ResponseEntity.ok(customerService.findByPhone(phone));
        }
        if (email != null) {
            return ResponseEntity.ok(customerService.findByEmail(email));
        }
        log.error("Неправильные параметры запроса");
        return ResponseEntity.badRequest().build();
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @PostMapping("/contact/add")
    public ResponseEntity<?> addContact(@Valid @RequestBody ContactDTO contactDTO, Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        customerService.addContact(contactDTO, customer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contact/delete")
    public ResponseEntity<?> deleteContact(@Valid @RequestBody ContactDTO contactDTO, Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        if (contactDTO.getPhone() != null) {
            customerService.deletePhone(contactDTO.getPhone(), customer);
            return ResponseEntity.ok().build();
        }
        if (contactDTO.getEmail() != null) {
            customerService.deleteEmail(customer.getEmail(), customer);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/contact/update")
    public ResponseEntity<?> updateContact(@Valid @RequestBody ContactUpdateDTO contactUpdateDTO, @RequestParam(required = false) String type, Authentication authentication) {
        var customer = AuthUtils.getCustomer(authentication);
        if (type != null && type.equals("additional")) {
            customerService.updateAdditionalContact(contactUpdateDTO, customer);
            return ResponseEntity.ok().build();
        }
        customerService.updateContact(contactUpdateDTO, customer);
        return ResponseEntity.ok().build();
    }
}

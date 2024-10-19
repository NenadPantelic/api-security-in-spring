package com.np.apisecurity.api.server;

import com.np.apisecurity.dto.NewCustomerFullName;
import com.np.apisecurity.entity.Customer;
import com.np.apisecurity.repository.CustomerDangerousRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sqlinjection/danger/v1")
@Validated
public class CustomerDangerousApi {

    private final CustomerDangerousRepository customerDangerousRepository;

    public CustomerDangerousApi(CustomerDangerousRepository customerDangerousRepository) {
        this.customerDangerousRepository = customerDangerousRepository;
    }

    @PostMapping(value = "/customers")
    public void createUser(@RequestBody @Valid Customer customer) {
        customerDangerousRepository.createCustomer(customer);
    }

    @GetMapping(value = "/customers/{email}")
    public List<Customer> getUserByEmail(@PathVariable("email") @Email String email) {
        return customerDangerousRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers")
    public List<Customer> getUserByGender(@RequestParam("gender")
                                          @Pattern(regexp = "^[MF]$", message = "Invalid gender.") String gender) {
        return customerDangerousRepository.findByGender(gender);
    }

    @PatchMapping(value = "/customers/{id}")
    public void updateCustomerFullName(@PathVariable("id") int id, @RequestBody NewCustomerFullName newCustomerFullName) {
        customerDangerousRepository.updateCustomerFullName(id, newCustomerFullName.fullName());
    }
}

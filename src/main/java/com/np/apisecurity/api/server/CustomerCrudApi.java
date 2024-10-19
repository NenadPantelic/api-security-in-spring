package com.np.apisecurity.api.server;

import com.np.apisecurity.dto.NewCustomerFullName;
import com.np.apisecurity.entity.Customer;
import com.np.apisecurity.repository.CustomerCrudRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sqlinjection/crud/v1")
@Validated
public class CustomerCrudApi {

    // CRUD API is safe from SQL injection as it used the prepared statements
    private final CustomerCrudRepository customerCrudRepository;

    public CustomerCrudApi(CustomerCrudRepository customerCrudRepository) {
        this.customerCrudRepository = customerCrudRepository;
    }

    @PostMapping(value = "/customers")
    public void createUser(@RequestBody @Valid Customer customer) {
        customerCrudRepository.save(customer);
    }

    @GetMapping(value = "/customers/{email}") // safe code since it uses the prepared statements
    public Customer getUserByEmail(@PathVariable("email") @Email String email) {
        return customerCrudRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers") // safe code as it uses the prepared statements
    public List<Customer> getUserByGender(@RequestParam("gender")
                                          @Pattern(regexp = "^[MF]$", message = "Invalid gender.") String gender) {
        return customerCrudRepository.findByGender(gender);
    }

    @PatchMapping(value = "/customers/{id}") // vulnerable code
    public void updateCustomerFullName(@PathVariable("id") int id, @RequestBody NewCustomerFullName newCustomerFullName) {
        customerCrudRepository.updateCustomerFullName(id, newCustomerFullName.fullName());
    }
}

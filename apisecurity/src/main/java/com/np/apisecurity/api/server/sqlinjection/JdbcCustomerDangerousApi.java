package com.np.apisecurity.api.server.sqlinjection;

import com.np.apisecurity.dto.request.NewCustomerFullName;
import com.np.apisecurity.entity.sqlinjection.JdbcCustomer;
import com.np.apisecurity.repository.sqlinjection.JdbcCustomerDangerousRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sqlinjection/dangerous/v1")
@Validated
public class JdbcCustomerDangerousApi {

    private final JdbcCustomerDangerousRepository jdbcCustomerDangerousRepository;

    public JdbcCustomerDangerousApi(JdbcCustomerDangerousRepository jdbcCustomerDangerousRepository) {
        this.jdbcCustomerDangerousRepository = jdbcCustomerDangerousRepository;
    }

    @PostMapping(value = "/customers")
    public void createUser(@RequestBody @Valid JdbcCustomer jdbcCustomer) {
        jdbcCustomerDangerousRepository.createCustomer(jdbcCustomer);
    }

    @GetMapping(value = "/customers/{email}")
    public List<JdbcCustomer> getUserByEmail(@PathVariable("email") String email) {
        return jdbcCustomerDangerousRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers")
    public List<JdbcCustomer> getUserByGender(@RequestParam("gender") String gender) {
        return jdbcCustomerDangerousRepository.findByGender(gender);
    }

    @PatchMapping(value = "/customers/{id}")
    public void updateCustomerFullName(@PathVariable("id") int id, @RequestBody NewCustomerFullName newCustomerFullName) {
        jdbcCustomerDangerousRepository.updateCustomerFullName(id, newCustomerFullName.fullName());
    }
}

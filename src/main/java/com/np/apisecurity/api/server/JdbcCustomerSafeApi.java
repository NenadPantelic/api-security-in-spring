package com.np.apisecurity.api.server;

import com.np.apisecurity.dto.NewCustomerFullName;
import com.np.apisecurity.entity.JdbcCustomer;
import com.np.apisecurity.repository.JdbcCustomerDangerousRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Slf4j
//@RestController
//@RequestMapping("/api/sqlinjection/danger/v1")
//@Validated
public class JdbcCustomerSafeApi {

    private final JdbcCustomerDangerousRepository jdbcCustomerDangerousRepository;

    public JdbcCustomerSafeApi(JdbcCustomerDangerousRepository jdbcCustomerDangerousRepository) {
        this.jdbcCustomerDangerousRepository = jdbcCustomerDangerousRepository;
    }

    @PostMapping(value = "/customers")
    public void createUser(@RequestBody @Valid JdbcCustomer jdbcCustomer) {
        jdbcCustomerDangerousRepository.createCustomer(jdbcCustomer);
    }

    @GetMapping(value = "/customers/{email}")
    public List<JdbcCustomer> getUserByEmail(@PathVariable("email") @Email String email) {
        return jdbcCustomerDangerousRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers")
    public List<JdbcCustomer> getUserByGender(@RequestParam("gender")
                                          @Pattern(regexp = "^[MF]$", message = "Invalid gender.") String gender) {
        return jdbcCustomerDangerousRepository.findByGender(gender);
    }

    @PatchMapping(value = "/customers/{id}")
    public void updateCustomerFullName(@PathVariable("id") int id, @RequestBody NewCustomerFullName newCustomerFullName) {
        jdbcCustomerDangerousRepository.updateCustomerFullName(id, newCustomerFullName.fullName());
    }
}

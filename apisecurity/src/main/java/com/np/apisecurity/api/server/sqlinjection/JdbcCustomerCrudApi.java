package com.np.apisecurity.api.server.sqlinjection;

import com.np.apisecurity.dto.request.NewCustomerFullName;
import com.np.apisecurity.entity.sqlinjection.JdbcCustomer;
import com.np.apisecurity.repository.sqlinjection.JdbcCustomerCrudRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Slf4j
//@RestController
//@RequestMapping("/api/sqlinjection/crud/v1")
//@Validated
public class JdbcCustomerCrudApi {

    // CRUD API is safe from SQL injection as it used the prepared statements
    private final JdbcCustomerCrudRepository jdbcCustomerCrudRepository;

    public JdbcCustomerCrudApi(JdbcCustomerCrudRepository jdbcCustomerCrudRepository) {
        this.jdbcCustomerCrudRepository = jdbcCustomerCrudRepository;
    }

//    @PostMapping(value = "/customers")
//    public void createUser(@RequestBody @Valid JdbcCustomer jdbcCustomer) {
//        jdbcCustomerCrudRepository.save(jdbcCustomer);
//    }

    @GetMapping(value = "/customers/{email}") // safe code since it uses the prepared statements
    public JdbcCustomer getUserByEmail(@PathVariable("email") @Email String email) {
        return jdbcCustomerCrudRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers") // safe code as it uses the prepared statements
    public List<JdbcCustomer> getUserByGender(@RequestParam("gender")
                                          @Pattern(regexp = "^[MF]$", message = "Invalid gender.") String gender) {
        return jdbcCustomerCrudRepository.findByGender(gender);
    }

    @PatchMapping(value = "/customers/{id}") // vulnerable code
    public void updateCustomerFullName(@PathVariable("id") int id, @RequestBody NewCustomerFullName newCustomerFullName) {
        jdbcCustomerCrudRepository.updateCustomerFullName(id, newCustomerFullName.fullName());
    }
}

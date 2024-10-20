package com.np.apisecurity.api.server;

import com.np.apisecurity.entity.JpaCustomer;
import com.np.apisecurity.repository.JpaCustomerCrudRepository;
import com.np.apisecurity.repository.JpaCustomerDangerousDAO;
import jakarta.persistence.GeneratedValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/sqlinjection/dangerous/v2")
public class JpaCustomerDangerousApi {

    private final JpaCustomerCrudRepository jpaCustomerCrudRepository;
    private final JpaCustomerDangerousDAO jpaCustomerDangerousDAO;

    public JpaCustomerDangerousApi(JpaCustomerCrudRepository jpaCustomerCrudRepository,
                                   JpaCustomerDangerousDAO jpaCustomerDangerousDAO) {
        this.jpaCustomerCrudRepository = jpaCustomerCrudRepository;
        this.jpaCustomerDangerousDAO = jpaCustomerDangerousDAO;
    }

    @GetMapping(value = "/customers/{email}") // safe, use prepared statements
    public JpaCustomer findByEmail(@PathVariable(value = "email") String email) {
        return jpaCustomerCrudRepository.findByEmail(email);
    }

    @GetMapping(value = "/customers") // vulnerable
    public List<JpaCustomer> findByGender(@RequestParam(value = "gender") String gender) {
        return jpaCustomerDangerousDAO.findByGender(gender);
    }


}

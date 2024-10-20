package com.np.apisecurity.api.server;

import com.np.apisecurity.entity.JpaCustomer;
import com.np.apisecurity.repository.JpaCustomerCrudRepository;
import com.np.apisecurity.repository.JpaCustomerSafeDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/api/sqlinjection/safe/v2")
public class JpaCustomerSafeApi {

    private final JpaCustomerSafeDAO jpaCustomerSafeDAO;

    public JpaCustomerSafeApi(JpaCustomerCrudRepository jpaCustomerCrudRepository,
                              JpaCustomerSafeDAO jpaCustomerSafeDAO) {
        this.jpaCustomerSafeDAO = jpaCustomerSafeDAO;
    }

    @GetMapping(value = "/customers")
    public List<JpaCustomer> findByGender(@RequestParam(value = "gender") String gender) {
        return jpaCustomerSafeDAO.findByGender(gender);
    }


}

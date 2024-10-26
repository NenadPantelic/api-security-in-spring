package com.np.apisecurity.api.server.sqlinjection;

import com.np.apisecurity.entity.sqlinjection.JpaCustomer;
import com.np.apisecurity.repository.sqlinjection.JpaCustomerCrudRepository;
import com.np.apisecurity.repository.sqlinjection.JpaCustomerSafeDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@RestController
//@RequestMapping("/api/sqlinjection/safe/v2")
public class JpaCustomerSafeApi {

    private final JpaCustomerSafeDAO jpaCustomerSafeDAO;

    public JpaCustomerSafeApi(JpaCustomerCrudRepository jpaCustomerCrudRepository,
                              JpaCustomerSafeDAO jpaCustomerSafeDAO) {
        this.jpaCustomerSafeDAO = jpaCustomerSafeDAO;
    }

//    @GetMapping(value = "/customers")
//    public List<JpaCustomer> findByGender(@RequestParam(value = "gender") String gender) {
//        return jpaCustomerSafeDAO.findByGender(gender);
//    }


}

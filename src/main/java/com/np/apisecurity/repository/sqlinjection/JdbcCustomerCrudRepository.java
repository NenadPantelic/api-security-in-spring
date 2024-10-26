package com.np.apisecurity.repository.sqlinjection;

import com.np.apisecurity.entity.sqlinjection.JdbcCustomer;
//import org.springframework.data.jdbc.repository.query.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
//public interface JdbcCustomerCrudRepository extends CrudRepository<JdbcCustomer, Integer> {
public interface JdbcCustomerCrudRepository {

    JdbcCustomer findByEmail(String email);

    List<JdbcCustomer> findByGender(String gender);

    //    @Query("CALL update_customer(:customerId, :newFullName)")
    // parameter values are not filtered
    void updateCustomerFullName(@Param("customerId") int customerId, @Param("newFullName") String fullName);
}

package com.np.apisecurity.repository;

import com.np.apisecurity.entity.Customer;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCrudRepository extends CrudRepository<Customer, Integer> {

    Customer findByEmail(String email);

    List<Customer> findByGender(String gender);

    @Query("CALL update_customer(:customerId, :newFullName)")
        // parameter values are not filtered
    void updateCustomerFullName(@Param("customerId") int customerId, @Param("newFullName") String fullName);
}

package com.np.apisecurity.repository;

import com.np.apisecurity.entity.JpaCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCustomerCrudRepository extends CrudRepository<JpaCustomer, Integer> {

    // prepared statements are used behind the scene, so these  are safe
    JpaCustomer findByEmail(String email);


}

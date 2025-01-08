package com.np.apisecurity.repository.sqlinjection;

import com.np.apisecurity.entity.sqlinjection.JpaCustomer;
//import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaCustomerSafeDAO {

//    private final EntityManager entityManager;
//
//    public JpaCustomerSafeDAO(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    public List<JpaCustomer> findByGender(String gender) {
//        // some business logic to build query
//        var jpqlQuery = "FROM JpaCustomer WHERE gender = :gender";
//        var query = entityManager.createQuery(jpqlQuery, JpaCustomer.class).setParameter("gender", gender);
//        return query.getResultList();
//    }
}

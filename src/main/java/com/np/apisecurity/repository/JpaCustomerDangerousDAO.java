package com.np.apisecurity.repository;

import com.np.apisecurity.entity.JpaCustomer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaCustomerDangerousDAO {

    private final EntityManager entityManager;

    public JpaCustomerDangerousDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // =M' or 'x' = 'x
    public List<JpaCustomer> findByGender(String gender) {
        // some business logic to build query
        var jpqlQuery = String.format("FROM JpaCustomer WHERE gender = '%s'", gender);
        var query = entityManager.createQuery(jpqlQuery, JpaCustomer.class);
        return query.getResultList();
    }
}

package com.np.apisecurity.repository;

import com.np.apisecurity.entity.JdbcCustomer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCustomerDangerousRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCustomerDangerousRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createCustomer(JdbcCustomer newJdbcCustomer) {
        var sql = String.format("INSERT INTO jdbc_customer(full_name, email, birth_date, gender) VALUES('%s', '%s', '%s', '%s')",
                newJdbcCustomer.getFullName(), newJdbcCustomer.getEmail(), newJdbcCustomer.getBirthDate(), newJdbcCustomer.getGender()
        );
        jdbcTemplate.execute(sql);
    }

    public List<JdbcCustomer> findByEmail(String email) {
        var query = String.format("SELECT id, full_name, email, birth_date, gender FROM jdbc_customer WHERE email = '%s' AND " +
                "email IS NOT null", email);
        // queryForObject for a single record
        return jdbcTemplate.query(query, this::mapToCustomer);
    }

    public List<JdbcCustomer> findByGender(String gender) {
        var query = String.format("SELECT id, full_name, email, birth_date, gender FROM jdbc_customer WHERE gender = '%s' AND " +
                "gender IS NOT null", gender);
        return jdbcTemplate.query(query, this::mapToCustomer);
    }

    // targets a vulnerable stored procedure
    public void updateCustomerFullName(int id, String fullName) {
        var sql = String.format("CALL update_jdbc_customer(%d, %s)", id, fullName);
        jdbcTemplate.execute(sql);
    }

    private JdbcCustomer mapToCustomer(ResultSet rs, long rowNum) throws SQLException {
        var customer = new JdbcCustomer();
        Optional.ofNullable(rs.getDate("birth_date")).ifPresent(bd -> customer.setBirthDate(bd.toLocalDate()));
        customer.setId(rs.getInt("id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setGender(rs.getString("gender"));
        return customer;
    }
}

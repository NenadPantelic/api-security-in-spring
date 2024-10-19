package com.np.apisecurity.repository;

import com.np.apisecurity.entity.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerSafeRepository {

    private final JdbcTemplate jdbcTemplate;
    // to avoid having many ? in your query and preserve their order, use named template
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomerSafeRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createCustomer(Customer newCustomer) {
        var sql = "INSERT INTO customer(full_name, email, birth_date, gender) VALUES(:fullName, :email, :birthDate, :gender)";
        var sqlParameters = new MapSqlParameterSource()
                .addValue("fullName", newCustomer.getFullName())
                .addValue("email", newCustomer.getEmail())
                .addValue("birthDate", newCustomer.getBirthDate())
                .addValue("gender", newCustomer.getGender());
        namedParameterJdbcTemplate.update(sql, sqlParameters);
    }

    public List<Customer> findByEmail(String email) {
        var query = "SELECT id, full_name, email, birth_date, gender FROM customer WHERE email = ? AND email IS NOT null";
        // queryForObject for a single record
        return jdbcTemplate.query(query, this::mapToCustomer, email);
    }

    public List<Customer> findByGender(String gender) {
        var query = "SELECT id, full_name, email, birth_date, gender FROM customer WHERE gender = ? AND gender IS NOT null";
        return jdbcTemplate.query(query, this::mapToCustomer, gender);
    }

    // targets a vulnerable stored procedure
    public void updateCustomerFullName(int id, String fullName) {
        var sql = "CALL update_customer(:id, :fullName)";
        var sqlParameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("fullName", fullName);
        namedParameterJdbcTemplate.update(sql, sqlParameters);
    }

    private Customer mapToCustomer(ResultSet rs, long rowNum) throws SQLException {
        var customer = new Customer();
        Optional.ofNullable(rs.getDate("birth_date")).ifPresent(bd -> customer.setBirthDate(bd.toLocalDate()));
        customer.setId(rs.getInt("id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setGender(rs.getString("gender"));
        return customer;
    }
}

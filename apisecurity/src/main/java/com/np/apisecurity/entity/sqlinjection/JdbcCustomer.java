package com.np.apisecurity.entity.sqlinjection;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class JdbcCustomer {

    @Id
    private int id;
    private String fullName;
    @Email
    private String email;
    private LocalDate birthDate;
    @Pattern(regexp = "^[MF]$", message = "Invalid gender.")
    private String gender;
}

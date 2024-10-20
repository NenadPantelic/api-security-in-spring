package com.np.apisecurity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class JpaCustomer {

    @Id
    private int id;
    private String fullName;
    @Email
    private String email;
    private LocalDate birthDate;
    @Pattern(regexp = "^[MF]$", message = "Invalid gender.")
    private String gender;
}

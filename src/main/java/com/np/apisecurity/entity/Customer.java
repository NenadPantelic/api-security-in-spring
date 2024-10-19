package com.np.apisecurity.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Customer {

    @Id
    private int id;
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private String gender;
}

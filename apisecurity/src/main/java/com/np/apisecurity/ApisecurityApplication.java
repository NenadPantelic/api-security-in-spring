package com.np.apisecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApisecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApisecurityApplication.class, args);
    }

}

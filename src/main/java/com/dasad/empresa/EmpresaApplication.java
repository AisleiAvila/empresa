package com.dasad.empresa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"com.dasad.empresa"}
)
public class EmpresaApplication {
    public EmpresaApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(EmpresaApplication.class, args);
    }
}

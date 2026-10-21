package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API de A3Getafe",
        version = "SNAPSHOT",
        description = "API REST para consultar, crear, actualizar y borrar gatos registrados en PostgreSQL"))
    
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

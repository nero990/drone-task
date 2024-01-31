package com.nero.dronetask;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(info = @Info(title = "Drone Service API", version = "1.0", description = "Drone Service API documentation"))
@EnableScheduling
@SpringBootApplication
public class DroneTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneTaskApplication.class, args);
    }

}

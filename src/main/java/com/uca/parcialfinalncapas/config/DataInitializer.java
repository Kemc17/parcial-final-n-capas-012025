package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void run(String... args) throws Exception {
        try {
            userService.findByCorreo("user@test.com");
            System.out.println("El usuario 'user@test.com' ya existe.");
        } catch (Exception e) {
            UserCreateRequest user = UserCreateRequest.builder()
                    .nombre("Test User")
                    .correo("user@test.com")
                    .password("password123")
                    .nombreRol("USER")
                    .build();
            userService.save(user);
            System.out.println("Usuario 'user@test.com' creado exitosamente.");
        }

        try {
            userService.findByCorreo("tech@test.com");
            System.out.println("El usuario 'tech@test.com' ya existe.");
        } catch (Exception e) {

            UserCreateRequest tech = UserCreateRequest.builder()
                    .nombre("Test Tech")
                    .correo("tech@test.com")
                    .password("password456")
                    .nombreRol("TECH")
                    .build();
            userService.save(tech);
            System.out.println("Usuario 'tech@test.com' creado exitosamente.");
        }
    }
}

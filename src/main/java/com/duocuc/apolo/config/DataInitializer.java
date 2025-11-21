package com.duocuc.apolo.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.duocuc.apolo.models.Role;
import com.duocuc.apolo.models.User;
import com.duocuc.apolo.repositories.RoleRepository;
import com.duocuc.apolo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    @Override
    public void run(String... args) throws Exception {

        // --- Crear roles ---
        if (roleRepository.count() == 0) {
            Role admin = new Role(null, "ADMIN");
            Role pacient = new Role(null, "PACIENTE");

            roleRepository.save(admin);
            roleRepository.save(pacient);

            System.out.println("Roles creados.");
        }

        // --- Crear usuarios ---
        if (userRepository.count() == 0) {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            Role pacientRole = roleRepository.findByName("PACIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol PACIENTE no encontrado"));

            User adminUser = new User();
            adminUser.setName("Administrador");
            adminUser.setLastname("Administrador");
            adminUser.setEmail("admin@duoc.cl");
            adminUser.setPassword(passwordEncoder.encode("123456")); 
            adminUser.setRole(adminRole);

            User pacientUser = new User();
            pacientUser.setName("Paciente");
            adminUser.setLastname("Uno");
            pacientUser.setEmail("paciente@duoc.cl");
            pacientUser.setPassword(passwordEncoder.encode("123456"));
            pacientUser.setRole(pacientRole);

            userRepository.save(adminUser);
            userRepository.save(pacientUser);

            System.out.println("Usuarios iniciales creados.");
        }
    }
}

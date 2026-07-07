package com.vehiqon.features.onboarding.config;

import com.vehiqon.common.enums.Role;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }
            UserEntity admin = UserEntity.builder()
                    .firstName("System")
                    .lastName("Administrator")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .status(UserStatus.ACTIVE.name())
                    .isVerified(true)
                    .build();
            admin.getRoles().add(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("Admin account created.");
        };
    }
}

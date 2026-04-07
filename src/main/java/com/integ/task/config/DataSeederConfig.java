package com.integ.task.config;

import com.integ.task.entity.UserRole;
import com.integ.task.repository.UserRepository;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Jwt jwt = new Jwt(
                    "dummy-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"),
                    Map.of(
                            "sub", "admin",
                            "careProviderIDP", 1L   // ⚠️ important for your legacy method
                    )
            );

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(jwt, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(auth);

            // 🔥 SAVE USERS
            if (userRepository.findByUsername("madhav").isEmpty()) {
                userRepository.save(new UserRole(
                        null,
                        "madhav",
                        passwordEncoder.encode("madhav123"),
                        "USER"
                ));
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new UserRole(
                        null,
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "ADMIN"
                ));
            }

            // 🔥 CLEAR CONTEXT
            SecurityContextHolder.clearContext();
        };
    }
}
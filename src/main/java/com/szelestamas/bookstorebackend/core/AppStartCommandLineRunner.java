package com.szelestamas.bookstorebackend.core;

import com.szelestamas.bookstorebackend.api.user.AuthService;
import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppStartCommandLineRunner implements CommandLineRunner {
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.first_name}")
    private String first_name;

    @Value("${admin.last_name}")
    private String last_name;

    @Override
    public void run(String... args) throws Exception {
        User admin = new User(email, passwordEncoder.encode(password), first_name, last_name, UserRole.ADMIN);
        try {
            if (authService.loadUserByUsername(admin.login()) != null)
                System.out.println("Admin user ready to use: " + admin);
        } catch (UsernameNotFoundException e) {
            User savedAdmin = authService.signUp(admin);
            System.out.println("Admin user has been created and ready to use: " + savedAdmin);
        }
    }
}

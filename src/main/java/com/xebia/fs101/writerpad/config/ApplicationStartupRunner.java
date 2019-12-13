package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = userRepository.findByUsernameOrEmail("admin", "admin@123.com");
        if (Objects.isNull(admin)) {
            admin = new User.Builder()
                    .withUsername("admin")
                    .withEmail("admin@123.com")
                    .withPassword(passwordEncoder.encode("password"))
                    .withUserRole(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}

package com.ecommerce.userservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Repository.UserRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final static Logger log = LoggerFactory.getLogger(AdminSeeder.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.seeding.enabled:true}")
    private boolean seedingEnabled;

    
    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedingEnabled) {
            log.info("Admin seeding is disabled. Set 'admin.seeding.enabled=true' to enable");
            return;
        }

        if (userRepository.existsByUserRole(UserRole.ADMIN)) {
            log.info("ADMIN already exists. Skipping seeding.");
            return;
        }
        
        if (adminEmail == null || adminPassword == null) {
            log.warn("ADMIN_EMAIL and ADMIN_PASSWORD environment variables are not set. Skipping admin creation.");
            return;
        }

        User adminUser = new User();
        adminUser.setName("Administrador");
        adminUser.setUserEmail(adminEmail);
        adminUser.setUserPassword(passwordEncoder.encode(adminPassword));
        adminUser.setUserRole(UserRole.ADMIN);
        adminUser.setUserStatus(UserStatus.ATIVO);
        adminUser.setUserType(UserType.SYSTEM);
        
        userRepository.save(adminUser);
        log.info("Admin created successfully. Email: " + adminEmail);
    }

}

package com.shaun.incident_api.security;

import com.shaun.incident_api.entity.Alert;
import com.shaun.incident_api.entity.AppUser;
import com.shaun.incident_api.repository.AlertRepository;
import com.shaun.incident_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitRunner.class);

    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitRunner(UserRepository userRepository,
                          AlertRepository alertRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        logger.info(">>> DataInitializer running <<<");

        // Initialize users
        initUsers();

        // Initialize sample alerts
        initAlerts();

        logger.info(">>> DataInitializer completed <<<");
    }

    private void initUsers() {
        // Create admin user
        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            logger.info("Created admin user");
        }

        // Create regular user
        if (userRepository.findByUsername("user").isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
            logger.info("Created test user");
        }
    }

    private void initAlerts() {
        if (alertRepository.count() == 0) {
            List<Alert> alerts = List.of(
                    createAlert("Prometheus", "High CPU usage detected", "HIGH"),
                    createAlert("Prometheus", "Memory usage above 80%", "MEDIUM"),
                    createAlert("Datadog", "Service response time degraded", "LOW"),
                    createAlert("AWS", "EC2 instance health check failed", "HIGH"),
                    createAlert("PagerDuty", "On-call acknowledgment pending", "MEDIUM")
            );
            alertRepository.saveAll(alerts);
            logger.info("Created {} sample alerts", alerts.size());
        }
    }

    private Alert createAlert(String source, String message, String severity) {
        Alert alert = new Alert();
        alert.setSource(source);
        alert.setMessage(message);
        alert.setSeverity(severity);
        return alert;
    }
}
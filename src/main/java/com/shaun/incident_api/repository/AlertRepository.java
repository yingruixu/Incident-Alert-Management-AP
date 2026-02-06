package com.shaun.incident_api.repository;

import com.shaun.incident_api.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}


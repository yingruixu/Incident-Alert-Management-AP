# Incident & Alert Management API

This project is a simple Incident & Alert Management REST API designed to simulate
real-world operational workflows such as monitoring alerts, incident tracking, and lifecycle management.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL 8
- Docker & Docker Compose
- Maven

## Architecture
+---------------- Frontend (Vue 3) ----------------+
|  Dashboard - Incidents - Alerts - Monitoring      |
+------------------------+---------------------------+
| HTTP + JWT Auth
+------------------------v---------------------------+
|              Backend (Spring Boot 3)              |
|            REST API - Validation - JPA            |
+------------------------+---------------------------+
|
+------------------------v---------------------------+
|                       MySQL 8                      |
|              incidentdb - alertdb                 |
+--------------------------------------------------+

+-------------+      +-------------+      +-------------+
| Prometheus  |      |   Grafana   |      | Vite Dev    |
|  Metrics    |      | Dashboards  |      |  Server     |
+-------------+      +-------------+      +-------------+
# Features
Module	Description
Dashboard	Real-time stats cards + line chart (incident trend) + doughnut chart (severity distribution)
Incidents	Full CRUD, search, filter by status/severity, status timeline
Alerts	Alert rule management with enable/disable, metric thresholds
Monitoring	Infrastructure health: CPU / Memory / Disk per service, color-coded progress bars
Settings	User profile, role management (Admin / SRE / Developer / Viewer), timezone
Auth	JWT-based login, route guard, token refresh
Tech Stack
Layer	Technology
Frontend	Vue 3 + Vite + Element Plus + Chart.js + Pinia + Vue Router
Backend	Java 17 + Spring Boot 3 + Spring Data JPA
Database	MySQL 8
Monitoring	Prometheus (metrics) + Grafana (dashboards)
Container	Docker Compose
Getting Started
Option 1 -- Run with Docker (recommended)
# Build and start all services
mvn clean package -DskipTests
docker compose up -d

# Access points
http://localhost:3001        # Frontend (login required)
http://localhost:8080        # Backend API
http://localhost:9090        # Prometheus
http://localhost:3000        # Grafana

# Project Structure
frontend/
+-- src/
|   +-- api/              # Axios API layer
|   +-- views/            # Page components
|   |   +-- Login.vue     # JWT auth
|   |   +-- Dashboard.vue # Stats + charts
|   |   +-- Incidents.vue # Incident CRUD
|   |   +-- Alerts.vue    # Alert rules
|   |   +-- Monitoring.vue# Infra health
|   |   +-- Settings.vue  # User config
|   +-- router/           # Vue Router + auth guards

backend/
+-- controller/           # REST controllers
+-- service/              # Business logic
+-- repository/           # JPA repositories
+-- entity/              # Domain models
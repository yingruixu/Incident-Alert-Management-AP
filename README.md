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

The application follows a layered architecture:

- Controller: Handles HTTP requests and validation
- Service: Contains business logic
- Repository: Data access layer using Spring Data JPA
- Entity: Domain models mapped to database tables

Incident and Alert are modeled as separate domains to reflect real-world operational systems.

## Getting Started

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven

How to Run Locally
1. Start MySQL with Docker
docker compose up -d mysql


MySQL configuration:

Database: incidentdb

User: incident_user

Password: incident_pass

Port: 3306

Database tables are created automatically by Spring Boot JPA
(ddl-auto=update).

2. Run the Application
./mvnw spring-boot:run


Or run IncidentApplication directly in IntelliJ IDEA.

3. Health Check
GET http://localhost:8080/health


Response:

OK

API Examples
Create Incident
POST /incidents
Content-Type: application/json

{
  "title": "Database connection failure",
  "severity": "P1"
}

Get All Incidents
GET /incidents

Get Incident by ID
GET /incidents/{id}

Close Incident
POST /incidents/{id}/close

Create Alert
POST /alerts
Content-Type: application/json

{
  "source": "Prometheus",
  "message": "CPU usage exceeds threshold",
  "severity": "P2"
}

Get All Alerts
GET /alerts




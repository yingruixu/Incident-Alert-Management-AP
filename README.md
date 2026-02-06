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

## How to Run Locally

### 1. Start MySQL with Docker

1. Make sure Docker is running
2. Start MySQL container

   docker compose up -d mysql

Database configuration:

- Database: incidentdb
- User: incident_user
- Password: incident_pass
- Port: 3306

Tables will be created automatically by Spring Boot JPA.

---

### 2. Run the Application

1. Start Spring Boot application

   ./mvnw spring-boot:run

2. Or run `IncidentApplication` in IntelliJ IDEA

---

###

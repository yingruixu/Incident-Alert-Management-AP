# Incident & Alert Management API

Lightweight Incident & Alert Management system with a Vue 3 frontend, Spring Boot backend, MySQL storage, and Prometheus/Grafana for monitoring.

## Features
- Dashboard: real-time stats, incident trend chart, severity distribution
- Incidents: CRUD, search, filter, status timeline
- Alerts: rule management, thresholds, enable/disable
- Monitoring: service CPU/Memory/Disk metrics (Prometheus)
- Auth: JWT-based login, token refresh, route guards

## Tech Stack
- Frontend: Vue 3, Vite, Element Plus, Pinia, Chart.js
- Backend: Java 17, Spring Boot 3, Spring Data JPA
- Database: MySQL 8
- Monitoring: Prometheus + Grafana
- Container: Docker / Docker Compose
- Build: Maven

## Quickstart (recommended: Docker)
Prerequisites: Docker, Docker Compose, Java 17, Maven

1. Build backend artifact (skip tests for faster local start):

```bash
mvnw.cmd clean package -DskipTests
```

2. Start services with Docker Compose:

```bash
docker compose up -d
```

3. Access the apps:

- Frontend: http://localhost:3001 (login required)
- Backend API: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## Local development

- Backend (hot reload):

```bash
cd src/main/java
mvnw spring-boot:run
```

- Frontend (dev server):

```bash
cd frontend
npm install
npm run dev
```

## Configuration
- Backend properties: see `src/main/resources/application.properties` for default values and database settings.
- Kubernetes manifests are in the `k8s/` folder. Use overlays for environment-specific configs (e.g., `k8s/overlays/prod`).

## Kubernetes
- A base set of manifests is available at `k8s/base/` for backend, frontend, MySQL, and ingress.
- To deploy to a cluster, adjust secrets (JWT key, DB credentials) and apply manifests with `kubectl apply -k k8s/overlays/<env>`.


## Project structure 

- frontend/: Vue 3 application (Vite)
- src/: Spring Boot backend source
- k8s/: Kubernetes manifests
- docker-compose.yml: local multi-service stack


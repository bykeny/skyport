# Airport Management System

## Overview
This repository hosts an Airport Management System built for an Enterprise System Integration course. The backend is a set of Spring Boot microservices and a shared API gateway. The frontend is a Vue.js application. Infrastructure services include per-service PostgreSQL databases and Kafka for async messaging.

## Repository Structure
- backend/
  - api-gateway/
  - flight-scheduling-service/
  - gate-management-service/
  - passenger-checkin-service/
  - baggage-tracking-service/
  - retail-dutyfree-service/
  - notification-service/
- frontend/
- infrastructure/

## Local Infrastructure (Docker)
The root docker-compose.yml provides:
- 6 isolated Postgres instances (one per backend service)
- Kafka + Zookeeper for asynchronous messaging

### Default Database Ports
- Flight Scheduling: 5432
- Gate Management: 5433
- Passenger Check-in: 5434
- Baggage Tracking: 5435
- Retail/Duty-free: 5436
- Notification: 5437

### Default Credentials
- POSTGRES_USER: airport_user
- POSTGRES_PASSWORD: airport_pass

You can override these with environment variables when running Docker.

## Running the Stack
From the repository root:

```bash
docker-compose up -d
```

Check container status:

```bash
docker-compose ps
```

Stop and remove containers:

```bash
docker-compose down -v
```

## Next Steps
- Initialize each service with Spring Boot and connect it to its matching database.
- Build the Vue.js frontend inside frontend/.
- Add shared infrastructure scripts or configs under infrastructure/.

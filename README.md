# Airport Management System

## Overview
This repository contains an Airport Management System for an Enterprise System Integration course. The backend is organized as Spring Boot microservices with a shared API gateway placeholder, and the frontend contains a small checkpoint UI for passenger check-in and duty-free ordering. Local infrastructure uses one PostgreSQL database per service plus Kafka/Zookeeper for asynchronous communication.

## Repository Structure
- `backend/`
  - `api-gateway/` (planned)
  - `flight-scheduling-service/` (implemented)
  - `gate-management-service/` (implemented)
  - `passenger-checkin-service/` (implemented)
  - `baggage-tracking-service/` (implemented)
  - `notification-service/` (implemented)
  - `retail-dutyfree-service/` (implemented)
- `frontend/` (implemented checkpoint UI for passenger check-in)
- `infrastructure/` (shared configs and scripts)

## Implemented Services

| Service | Port | Database | Swagger UI | Notes |
| --- | ---: | --- | --- | --- |
| Flight Scheduling | 8081 | `jdbc:postgresql://localhost:5432/flight_scheduling` | `http://localhost:8081/swagger-ui.html` | Flight lifecycle, flight events to Kafka |
| Gate Management | 8082 | `jdbc:postgresql://localhost:5433/gate_management` | `http://localhost:8082/swagger-ui.html` | Gate assignment, consumes flight events |
| Passenger Check-in | 8083 | `jdbc:postgresql://localhost:5434/passenger_checkin` | `http://localhost:8083/swagger-ui.html` | Check-in flow, produces check-in events |
| Baggage Tracking | 8084 | `jdbc:postgresql://localhost:5435/baggage_tracking` | `http://localhost:8084/swagger-ui.html` | Baggage lifecycle, consumes check-in/flight events |
| Retail Duty-free | 8085 | `jdbc:postgresql://localhost:5436/retail_dutyfree` | `http://localhost:8085/swagger-ui.html` | Product catalog and checked-in passenger orders |
| Notification | 8086 | `jdbc:postgresql://localhost:5437/notification` | `http://localhost:8086/swagger-ui.html` | Notification delivery and templates |

## Local Infrastructure (Docker)
By default, the root `docker-compose.yml` starts **infrastructure only**:
- 6 isolated PostgreSQL containers, one per business service
- Kafka and Zookeeper for asynchronous communication

The Compose file also contains a couple of Spring Boot services for an **all-in-docker** workflow, but they are disabled by default via a Compose profile to avoid host port conflicts when you run services locally.

### Default Database Ports
- Flight Scheduling: `5432`
- Gate Management: `5433`
- Passenger Check-in: `5434`
- Baggage Tracking: `5435`
- Retail/Duty-free: `5436`
- Notification: `5437`

### Default Credentials
- `POSTGRES_USER=airport_user`
- `POSTGRES_PASSWORD=airport_pass`

You can override these with environment variables if needed.

## How to Run

### 1) Start the infrastructure
From the repository root:

```bash
docker compose up -d
```

(If you're using the older v1 plugin, use `docker-compose up -d` instead.)

Verify it is running:

```bash
docker-compose ps
```

### 2) Start a service
Each service can be started independently after the infrastructure is up.

If you have Java 21 and Maven installed locally:

```bash
cd backend/notification-service
mvn clean spring-boot:run
```

If you want to run services in Docker (instead of locally), enable the app containers:

```bash
docker compose --profile apps up -d
```

The app profile also serves the frontend at `http://localhost:5173`.

Important: don't run the same service both ways at the same time. If you see `Port 808X was already in use`, it usually means that service is already running in Docker and publishing that port. Either stop that container (e.g., `docker compose stop notification-service`) or run the local JVM on a different port.

If you prefer Docker for builds/runs on Windows:

```powershell
docker run --rm -v "$((Get-Location).Path):/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 mvn -f backend/notification-service/pom.xml clean test
```

Use the same pattern for the other backend services by replacing the service folder and port.

### 3) Open Swagger UI
After a service starts, open its Swagger UI in a browser:

- Flight Scheduling: `http://localhost:8081/swagger-ui.html`
- Gate Management: `http://localhost:8082/swagger-ui.html`
- Passenger Check-in: `http://localhost:8083/swagger-ui.html`
- Baggage Tracking: `http://localhost:8084/swagger-ui.html`
- Retail Duty-free: `http://localhost:8085/swagger-ui.html`
- Notification: `http://localhost:8086/swagger-ui.html`

OpenAPI JSON is available at `/api-docs` for services that expose springdoc.

### 4) Test service endpoints
Swagger is the fastest way to test endpoints because request/response payloads are documented there.

You can also use `curl` or Postman. Example health checks:

```bash
curl http://localhost:8083/actuator/health
curl http://localhost:8086/actuator/health
```

Example endpoint tests:

```bash
# Create a flight first. Passenger Check-in validates this flight through the
# Flight Scheduling service when the Docker app profile is used.
curl -X POST http://localhost:8081/api/v1/flights \
  -H "Content-Type: application/json" \
  -d '{"flightNumber":"BT101","origin":"TLL","destination":"RIX","scheduledDeparture":"2026-06-01T10:00:00","scheduledArrival":"2026-06-01T11:00:00","aircraftType":"A220","airlineCode":"BT"}'

# Passenger Check-in. Replace flightId with the ID returned by the flight call.
curl -X POST http://localhost:8083/api/v1/checkins \
  -H "Content-Type: application/json" \
  -d '{"passengerId":1,"flightId":1,"seatNumber":"12A","baggageCount":1}'

curl http://localhost:8083/api/v1/checkins/flight/1

# Retail Duty-free
curl -X POST http://localhost:8085/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"DF-CHOC-01","name":"Tallinn Chocolate Box","category":"Confectionery","price":12.50,"stockQuantity":20}'

curl -X POST http://localhost:8085/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"passengerId":1,"flightId":1,"productId":1,"quantity":1}'

# Notification
curl -X POST http://localhost:8086/api/v1/notifications \
  -H "Content-Type: application/json" \
  -d '{"recipientId":1,"recipientType":"PASSENGER","channel":"EMAIL","subject":"Test","body":"Hello"}'
```

## Checkpoint 2 Demo

Run the backend services and frontend:

```bash
docker compose --profile apps up -d
```

Demonstrate the real integration:
- Flight Scheduling runs on `http://localhost:8081`.
- Passenger Check-in runs on `http://localhost:8083`.
- In the app profile, `FLIGHT_VALIDATION_ENABLED` defaults to `true`, so `POST /api/v1/checkins` calls Flight Scheduling before saving the check-in.
- Retail Duty-free runs on `http://localhost:8085`.
- In the app profile, `CHECKIN_VALIDATION_ENABLED` defaults to `true`, so `POST /api/v1/orders` calls Passenger Check-in before saving a duty-free order.

Demonstrate the frontend:
- Open `http://localhost:5173`.
- Load check-ins for a flight, or create a check-in for an existing flight ID.
- Add a duty-free product, load products, or create an order for a checked-in passenger.
- The check-in table is populated from `GET http://localhost:8083/api/v1/checkins/flight/{flightId}`.
- The product table is populated from `GET http://localhost:8085/api/v1/products`.

## Testing
The implemented services include controller tests using `@WebMvcTest` and mocked dependencies.

Run the tests for a service:

```bash
cd backend/flight-scheduling-service
mvn test
```

Repeat for the other services:
- `backend/gate-management-service`
- `backend/passenger-checkin-service`
- `backend/baggage-tracking-service`
- `backend/retail-dutyfree-service`
- `backend/notification-service`

Expected coverage pattern:
- at least one happy-path test
- at least one error-case test
- mocked dependency or service layer interaction

## Notes
- `api-gateway/` is currently a placeholder.
- The frontend is a static checkpoint UI served from `frontend/`.

## Next Steps
- Implement the API Gateway.
- Replace or extend the checkpoint frontend with the final frontend application.

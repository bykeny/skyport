# **SkyPort**
**SkyPort – An Integrated Enterprise Microservices System.**

## **Big Bang Startup (Checkpoint #3)**
Run the entire system from the repository root:

```bash
docker-compose --profile apps up --build
```

Note: If you use Docker Compose v2, replace `docker-compose` with `docker compose`.

## **Team & Responsibilities**
| Team Member | Services / Integration Components |
| --- | --- |
| Ibrahim | Flight Scheduling, Gate Management |
| Umid | Passenger Check-in, Security Clearance |
| Tofig | Baggage Tracking, Retail Duty-free |
| Kanan | API Gateway, Kafka, Auth Service |

## **System Ports (Quick Reference)**
- Frontend: 5173
- API Gateway: 8080
- Auth Service: 8088
- Flight Scheduling: 8081
- Gate Management: 8082
- Passenger Check-in: 8083
- Baggage Tracking: 8084
- Retail Duty-free: 8085
- Notification Service: 8086
- Security Clearance: 8087

## **Swagger URLs**
- Flight Scheduling: http://localhost:8081/swagger-ui.html
- Gate Management: http://localhost:8082/swagger-ui.html
- Passenger Check-in: http://localhost:8083/swagger-ui.html
- Baggage Tracking: http://localhost:8084/swagger-ui.html
- Retail Duty-free: http://localhost:8085/swagger-ui.html
- Notification Service: http://localhost:8086/swagger-ui.html
- Security Clearance: http://localhost:8087/swagger-ui.html
- Auth Service: http://localhost:8088/swagger-ui.html

## **Tech Stack**
- Spring Boot 3
- Vue 3
- PostgreSQL
- Kafka
- Docker

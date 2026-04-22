# Job Radar

Job Radar is a backend project built with Java and Spring Boot to collect job postings from company career pages, normalize the data, store it in PostgreSQL, and expose it through a simple API.

## 🚀 Features

- Import jobs from external sources (Greenhouse API)
- Normalize job data into a consistent internal model
- Deduplicate jobs based on unique URL
- Store jobs in PostgreSQL
- Filter jobs by keyword and location
- REST API for accessing job data

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker Compose
- JUnit / Mockito

## 📡 API Endpoints

### Create Job
```http
POST /jobs
GET /jobs
GET /jobs/{id}
POST /jobs/import
POST /jobs/import/greenhouse

```

## Current Connector
Greenhouse Job Board API
Fetches real job postings from external company boards
Maps external JSON into internal Job entities
Handles missing fields (e.g., location fallback)
Uses URL as a unique identifier for deduplication

## Architecture Flow
Client → HTTP Request (JSON)
→ Spring Boot (Controller Layer)
→ DTO (JobRequest)
→ Service Layer (business logic + deduplication)
→ Repository (Spring Data JPA)
→ Hibernate (ORM)
→ PostgreSQL

Response flow:
PostgreSQL → Hibernate → Service → Controller → DTO (JobResponse) → JSON → Client

## Import flow
External API (Greenhouse)
→ Connector
→ External DTOs
→ Job Entity
→ Service.save() (deduplication)
→ PostgreSQL
→ API Response

## Project Status
Actively in development.

Current focus:

- improving test coverage
- enhancing connectors
- adding export functionality (CSV)
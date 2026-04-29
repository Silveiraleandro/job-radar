# Job Radar

Job Radar is a backend project built with Java and Spring Boot to collect job postings from company career pages, 
normalize the data, store it in PostgreSQL, and expose it through a simple API.

## Features

- Create and retrieve job postings through a REST API
- Import jobs from a mock connector
- Import real jobs from the Greenhouse Job Board API
- Import jobs from an HTML scraping connector using Jsoup
- Normalize all sources into a common Job model
- Deduplicate jobs by URL
- Filter jobs by location or keyword
- Export saved jobs as a downloadable CSV file
- Unit tests for services and connectors

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Docker Compose
- Jsoup
- JUnit 5
- Mockito

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/jobs` | Create a job manually |
| GET | `/jobs` | Get all jobs |
| GET | `/jobs?location=Vancouver` | Filter jobs by location |
| GET | `/jobs?keyword=java` | Filter jobs by keyword |
| GET | `/jobs/{id}` | Get a job by ID |
| POST | `/jobs/import` | Import jobs from mock connector |
| POST | `/jobs/import/greenhouse` | Import jobs from Greenhouse API |
| POST | `/jobs/import/html` | Import jobs from HTML scraping connector |
| GET | `/jobs/export` | Download jobs as CSV |


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
→ Connector (API / HTML / Mock)
→ External DTOs
→ Job Entity
→ JobService.importJobs()
→ Deduplication by URL
→ PostgreSQL
→ API Response

## Running locally
→ Start PostgreSQL
docker compose up -d
→ Run the application
./mvnw spring-boot:run

## Project Status
MVP completed with multiple connectors, CSV export, and unit test coverage.

Next Improvements
Live production scraping source
Pagination
Combined filtering
Additional connectors
CI/CD pipeline
Swagger / OpenAPI docs

### Export Jobs to CSV
```http
GET /jobs/export
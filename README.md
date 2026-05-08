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
- Filter jobs by location and/or keyword
- Pagination support for large result sets
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
| GET | `/jobs?page=0&size=20` | Get paginated jobs |
| GET | `/jobs?location=Canada&keyword=Engineer&page=0&size=10` | Combined filtering with pagination |


## Connectors

## Connectors

### Mock Connector
Used for local testing and predictable demo data.

### Greenhouse API Connector
- Fetches real job postings from Greenhouse job boards
- Maps external JSON into internal Job entities
- Handles missing fields (e.g., location fallback)

### HTML Scraping Connector
- Uses Jsoup to scrape live Lever-hosted job boards
- Extracts titles, locations, and URLs from HTML
- Uses CSS selectors for parsing

## Architecture Flow

Client
→ HTTP Request
→ Spring MVC Controller
→ DTO Mapping / Validation
→ Service Layer
→ Repository Layer
→ Hibernate / JPA
→ PostgreSQL

Response:
PostgreSQL
→ Hibernate
→ Service
→ Controller
→ Response DTO
→ JSON Response
→ Client

## Import flow
→ Connector (API / HTML / Mock)
→ External Source Mapping 
→ Job Entity
→ JobService.importJobs()
→ Deduplication by URL
→ PostgreSQL
→ API Response

## Running locally

### Start PostgreSQL

```bash
docker compose up -d

./mvnw spring-boot:run
```

## Project Status

Current MVP includes:

- REST API with layered Spring Boot architecture
- PostgreSQL persistence with JPA/Hibernate
- DTO validation and entity mapping
- URL-based deduplication
- Pagination and combined filtering
- CSV export support
- Multiple job ingestion connectors
- Real Greenhouse API integration
- Live HTML scraping using Jsoup
- Unit tests with JUnit and Mockito
- Externalized YAML configuration

The application now functions as a multi-source job aggregation backend platform.

## Future Improvements

- Swagger / OpenAPI documentation
- Scheduled job imports
- Additional connectors
- CI/CD pipeline
- Frontend dashboard

### Export Jobs to CSV

```http
GET /jobs/export
```

Returns a downloadable CSV file containing all stored jobs.
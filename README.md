# Job Radar

Job Radar is a backend project built with Java and Spring Boot to collect job postings from company career pages, normalize the data, store it in PostgreSQL, and expose it through a simple API.

## Goals
- Practice backend design with Java and Spring Boot
- Build a clean and maintainable portfolio project
- Aggregate job postings from selected sources
- Support filtering, deduplication, and CSV export

## Planned Stack
- Java
- Spring Boot
- PostgreSQL
- Docker Compose
- JUnit / Mockito

## Status
Project setup phase.

## Architecture Flow
Client (Postman / Frontend)
→ sends HTTP request (JSON)

Spring Boot (Tomcat)
→ receives request
→ maps endpoint (/jobs, POST)
→ converts JSON → JobRequest (DTO) using Jackson
→ validates DTO (@Valid)

Controller
→ receives JobRequest
→ maps DTO → Job (Entity)
→ calls JobService e.g JobService.save(job)

Service
→ checks if job exists (findByUrl)
→ if exists: log + return existing job
→ if not: log + save job

Repository (Spring Data JPA)
→ delegates to Hibernate

Hibernate (ORM)
→ translates Java → SQL
→ executes SQL in PostgreSQL
→ maps result → Job entity (with ID)

Service
→ returns Job entity

Controller
→ maps Job → JobResponse (DTO)

Spring Boot
→ converts JobResponse → JSON (Jackson)
→ sends HTTP response

Client
→ receives JSON response

# Employee Management System

A REST API built with Spring Boot, Spring Data JPA (Hibernate), MySQL, and JWT Authentication.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Spring Security + JWT
- Lombok
- Bean Validation
- Maven


**Completed:**
- Project setup and layered architecture (controller, service, repository, entity, dto, config, security)
- `Employee` and `Department` entities with One-to-Many / Many-to-One relationship
- JWT-based Authentication (Register, Login, Token generation & validation, BCrypt password encryption, Role-based access — ADMIN / USER)

**In Progress:**
- Employee CRUD APIs (Create, Update, Delete, Search, Pagination, Sorting)

**Next Planned:**
- Global Exception Handling
- Swagger/OpenAPI Documentation
- Postman Collection

> Full Swagger documentation for all APIs will be added later. This section only covers what's functional so far.

## APIs Completed So Far

### 1. Register — `POST /api/auth/register`

Creates a new user account with a role (`ADMIN` or `USER`).

**Request Body**
```json
{
  "username": "admin1",
  "password": "Pass@123",
  "role": "ADMIN"
}
```

**Response**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null,
  "timestamp": "2026-07-22T10:30:00"
}
```

### 2. Login — `POST /api/auth/login`

Authenticates a user and returns a JWT token to be used in the `Authorization` header (`Bearer <token>`) for all protected endpoints.

**Request Body**
```json
{
  "username": "admin1",
  "password": "Pass@123"
}
```

**Response**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEi...",
    "username": "admin1",
    "role": "ROLE_ADMIN"
  },
  "timestamp": "2026-07-22T10:31:00"
}
```
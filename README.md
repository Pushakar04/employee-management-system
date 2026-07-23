# Employee Management System

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
- Project setup and layered architecture (controller, service, repository, entity, dto, config, security, exception, util)
- `Employee` and `Department` entities with One-to-Many / Many-to-One relationship
- JWT-based Authentication (Register, Login, Role-based access: ADMIN / USER)
- Employee CRUD APIs
- Bean Validation on all Employee and Auth request fields with meaningful error messages
- Global Exception Handling
- SLF4J logging
- JUnit tests

**Planned:**
- Swagger/OpenAPI documentation
- Postman collection export

## Common API Response Format

Every API returns a consistent response shape, on both success and failure:

```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {},
  "timestamp": "2026-07-23T10:30:00"
}
```

On errors, `success` is `false`, `data` is typically `null` (or a list of field errors for validation failures), and `message` describes what went wrong.

## Authentication APIs

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

## Department APIs

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/departments` | ADMIN | Create a department |
| GET | `/api/departments` | Authenticated | Get all departments |

## Employee APIs

All endpoints require a valid JWT (`Authorization: Bearer <token>`). Write operations (create/update/delete) require the `ADMIN` role; read/search operations are available to both `ADMIN` and `USER`.

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/employees` | ADMIN | Create a new employee |
| GET | `/api/employees/{id}` | Authenticated | Get employee by ID |
| GET | `/api/employees` | Authenticated | Get all employees (paginated, sortable) |
| PUT | `/api/employees/{id}` | ADMIN | Update an employee |
| DELETE | `/api/employees/{id}` | ADMIN | Soft delete (marks status as `INACTIVE`) |
| GET | `/api/employees/search/name?name=` | Authenticated | Search employees by name (partial match) |
| GET | `/api/employees/search/department?departmentName=` | Authenticated | Search employees by department (partial match) |

**Example: Create Employee**

**Request Body**
```json
{
    "employeeName" : "John Doe",
    "email" : "johndoe123@gmail.com",
    "mobileNumber" : "1234907892",
    "designation" : "Intern",
    "salary" : 120000.00,
    "dateOfJoining" : "2026-07-23",
    "departmentId": 3
}
```

**Response**
```json
{
    "success": true,
    "message": "Employee created successfully",
    "data": {
        "employeeId": 3,
        "employeeName": "John Doe",
        "email": "johndoe123@gmail.com",
        "mobileNumber": "1234907892",
        "designation": "Intern",
        "salary": 120000.0,
        "dateOfJoining": "2026-07-23",
        "status": "ACTIVE",
        "departmentId": 3,
        "departmentName": "Botanical"
    },
    "timestamp": "2026-07-23T17:56:46.0765817"
}
```

**Pagination & Sorting example:**
```
GET /api/employees?page=0&size=10&sort=salary,desc
```

## Error Handling

All exceptions are handled centrally and return the standard response format above with an appropriate HTTP status:

| Scenario | Status |
|---|---|
| Resource not found (e.g. invalid employee/department ID) | 404 |
| Duplicate record (e.g. email or username already exists) | 409 |
| Validation failure (e.g. blank name, invalid email, invalid mobile number) | 400 |
| Invalid login credentials | 401 |
| Role not authorized for the action (e.g. USER attempting a write) | 403 |
| Unexpected server error | 500 |

**Example: Validation Error**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": [
    { "field": "email", "message": "Email must be a valid email address" },
    { "field": "mobileNumber", "message": "Mobile number must contain exactly 10 digits" }
  ],
  "timestamp": "2026-07-23T10:05:00"
}
```
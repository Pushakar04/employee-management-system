# Employee Management System

#### Backend : Railway.app
#### Frontend : vercel

| Component | URL |
|---|---|
| Swagger / API Docs | https://employee-management-system-production-9d88.up.railway.app/swagger-ui/index.html |
| Frontend (React) | https://employee-management-system-sand-omega.vercel.app |

### Tech Stack

**Backend:** Java 17, Spring Boot, Spring Data JPA (Hibernate), MySQL, Spring Security + JWT, Lombok, Bean Validation, Maven, Swagger/OpenAPI

**Frontend:** React 18 (Vite), react-router-dom, axios

### Status: Completed

- Project setup and layered architecture (controller, service, repository, entity, dto, config, security, exception, util)
- `Employee` and `Department` entities with One-to-Many / Many-to-One relationship
- JWT-based Authentication: Register, Login, Token generation & validation, BCrypt password encryption, Role-based access (ADMIN / USER)
- Employee CRUD APIs: Create, Get by ID, Get All, Update, Soft Delete, Search by Name, Search by Department, Pagination and Sorting
- Department APIs: Create, Get All
- Bean Validation on all request fields with meaningful error messages
- Global Exception Handling (`@ControllerAdvice`): standardized error responses for Resource Not Found, Duplicate Record, Validation, Unauthorized, and Forbidden cases
- SLF4J logging for incoming requests, successful operations, and exceptions
- Swagger/OpenAPI documentation for all endpoints
- JUnit tests for JWT utility, user details service, and Employee service
- Basic React frontend (login, employee CRUD, department view) connected via REST
- Deployed: Spring Boot and MySQL on Railway, React frontend on Vercel

## Common API Response Format

Every API returns a consistent response shape, on both success and failure:

```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {},
  "timestamp": "2026-07-24T10:30:00"
}
```

On errors, `success` is `false`, `data` is typically `null` (or a list of field errors for validation failures), and `message` describes what went wrong.

## Authentication APIs

### 1. Register — `POST /api/auth/register`

**Request Body**
```json
{
  "username": "priya.sharma",
  "password": "SecurePass@456",
  "role": "USER"
}
```

**Response**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null,
  "timestamp": "2026-07-24T09:15:00"
}
```

### 2. Login — `POST /api/auth/login`

Returns a JWT token to be used in the `Authorization` header (`Bearer <token>`) for all protected endpoints.

**Request Body**
```json
{
  "username": "priya.sharma",
  "password": "SecurePass@456"
}
```

**Response**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcml5YS5zaGFybWEi...",
    "username": "priya.sharma",
    "role": "ROLE_USER"
  },
  "timestamp": "2026-07-24T09:16:00"
}
```

## Department APIs

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/department` | ADMIN | Create a department |
| GET | `/api/department` | Authenticated | Get all departments |

**Example — Create Department**
```json
{
  "departmentName": "Finance"
}
```

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
  "employeeName": "Ananya Iyer",
  "email": "ananya.iyer@example.com",
  "mobileNumber": "9123456780",
  "designation": "Business Analyst",
  "salary": 62000,
  "dateOfJoining": "2023-11-10",
  "departmentId": 2
}
```

**Response**
```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {
    "employeeId": 7,
    "employeeName": "Ananya Iyer",
    "email": "ananya.iyer@example.com",
    "mobileNumber": "9123456780",
    "designation": "Business Analyst",
    "salary": 62000,
    "dateOfJoining": "2023-11-10",
    "status": "ACTIVE",
    "departmentId": 2,
    "departmentName": "Finance"
  },
  "timestamp": "2026-07-24T09:20:00"
}
```

**Pagination & Sorting example:**
```
GET /api/employees?page=0&size=10&sort=salary,desc
```

**Search example:**
```
GET /api/employees/search/department?departmentName=Finance
```

## Error Handling

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
    { "field": "mobileNumber", "message": "Mobile number must contain exactly 10 digits" },
    { "field": "salary", "message": "Salary must be a positive value" }
  ],
  "timestamp": "2026-07-24T09:25:00"
}
```

**Example: Resource Not Found**
```json
{
  "success": false,
  "message": "Employee not found with id: 99",
  "data": null,
  "timestamp": "2026-07-24T09:26:00"
}
```
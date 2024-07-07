# Task 2 Backend Project

This project implements user authentication, organization management, and associated endpoints using Java/Spring.

## Directory Structure

### Main Code

The main code for the project can be found under `hng-task-2/src/main/java/com/victor/hng-task-2/`, organized into the following folders:

- **controller**: Contains API endpoints handling HTTP requests.
- **dto**: Data Transfer Objects for mapping between entities and API requests/responses.
- **entity**: Entity classes representing database tables.
- **exception**: Custom exception classes for handling errors.
- **jwt**: JWT service for authentication and authorization.
- **mapper**: Mapper classes for converting between DTOs and entities.
- **repository**: Data access layer interfaces.
- **security**: Security configuration and utilities.
- **services**: Business logic services.
- **HngTask2Application.java**: Entry point for the Spring Boot application.

### Tests

Tests for the project are located in `hng-task-2/src/test/java/com/victor/hng-task-2/auth/`. Here are the test files provided:

- **E2ETest.java**: End-to-end tests covering user registration, login, and organization operations.
- **JwtServiceTest.java**: Unit tests for JWT token generation and validation.
- **OrganisationServiceTest.java**: Tests for organization creation and user membership operations.

### Example Test Scenarios

- **Register User Successfully with Default Organisation**: Ensure registration works and default organization name is correctly generated.
- **Login User Successfully**: Verify successful login with correct credentials.
- **Handle Missing Required Fields**: Test registration failure for missing required fields.
- **Handle Duplicate Email or UserID**: Verify proper error handling for duplicate email or userID.

## Getting Started

To run the project locally or contribute, follow these steps:

1. Clone this repository.
2. Set up your environment variables or configuration files (`application.yml`, environment variables for secrets).
3. Build and run the project using your preferred IDE or build tool.

# Task Manager API

A simple RESTful Task Management backend built with Spring Boot, Spring Data JPA, PostgreSQL, and JWT-based authentication.

## Features

* **User registration & login** with email, username, and password (stored securely as hashes).
* **JWT authentication** (HS512) to protect all task-related endpoints.
* **CRUD operations** for user-specific tasks:

    * Create, read, update, delete individual tasks
    * List all tasks for the authenticated user
    * Delete all tasks for the user
* Basic validation and error handling.

## Tech Stack

* **Java 21**
* **Spring Boot 3.5.3**
* **Spring Security**
* **Spring Data JPA**
* **PostgreSQL**
* **JJWT** for JWT handling
* **Lombok** for boilerplate reduction

## Prerequisites

* Java 21 (or higher)
* Maven 3.8+
* PostgreSQL 14+

**NOTE**: Please set up Postgresql on your machine before proceeding

## Getting Started

1. **Clone the repository**

   ```bash
   git clone https://github.com/afaafhariri/task-manager-spring-boot.git
   cd task-manager-spring-boot/server
   ```

2. **Configure the database**

   Create a PostgreSQL database and a dedicated user:

   ```sql
   CREATE DATABASE taskmanager;
   CREATE USER tmuser WITH PASSWORD 'secret';
   GRANT ALL PRIVILEGES ON DATABASE taskmanager TO tmuser;
   ```

3. **Set environment variables**

   The application reads its configuration from environment variables or `application.properties`. Required:

   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskmanager
   export SPRING_DATASOURCE_USERNAME=tmuser
   export SPRING_DATASOURCE_PASSWORD=secret

   # JWT secret must be at least 512 bits (use base64 or Keys.secretKeyFor(SignatureAlgorithm.HS512))
   export JWT_SECRET=<your-512-bit-base64-secret>
   ```

4. **Build & Run**

   ```bash
   mvn clean compile spring-boot:run
   ```

   The server will start on `http://localhost:8080`.

## API Endpoints

### Authentication

#### Register

* **URL:** `POST /api/auth/register`

* **Request Body:**

  ```json
  {
    "username": "alice",
    "email": "alice@example.com",
    "password": "P@ssw0rd"
  }
  ```

* **Response:**

    * `201 Created` on success
    * `400 Bad Request` on validation errors

#### Login

* **URL:** `POST /api/auth/login`

* **Request Body:**

  ```json
  {
    "username": "alice",
    "password": "P@ssw0rd"
  }
  ```

* **Response:**

    * `200 OK` with JWT token

      ```json
      { "token": "<JWT_TOKEN>" }
      ```
    * `401 Unauthorized` on invalid credentials

### Tasks (JWT required)

Include header: `Authorization: Bearer <JWT_TOKEN>`

#### List Tasks

* **URL:** `GET /api/tasks`
* **Response:**

    * `200 OK`

      ```json
      [
        { "id": 1, "title": "Buy milk", "description": "2 liters", "completed": false },
        { "id": 2, "title": "Read book", "description": "Chapter 4", "completed": true }
      ]
      ```

#### Get Task by ID

* **URL:** `GET /api/tasks/{id}`
* **Response:**

    * `200 OK` with task object
    * `404 Not Found` if not owned by user

#### Create Task

* **URL:** `POST /api/tasks`
* **Request Body:**

  ```json
  { "title": "My First Task", "description": "..." }
  ```
* **Response:**

    * `201 Created` with created task

#### Update Task

* **URL:** `PUT /api/tasks/{id}`
* **Request Body:**

  ```json
  { "title": "Updated", "description": "...", "completed": true }
  ```
* **Response:**

    * `200 OK` with updated task

#### Delete Task

* **URL:** `DELETE /api/tasks/{id}`
* **Response:**

    * `204 No Content` on success
    * `404 Not Found` if not owned by user

#### Delete All Tasks

* **URL:** `DELETE /api/tasks`
* **Response:**

    * `204 No Content`
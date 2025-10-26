# JackedLabs: The AI-Powered Fitness Tracker

JackedLabs is a comprehensive, AI-powered fitness tracking application built on a modern Java microservice architecture. It goes beyond simple logging by integrating Google's Gemini AI to provide users with intelligent suggestions, nutritional analysis, and progress summaries to help them reach their fitness goals.



---

## Features

-   **Secure User Management**: Full authentication and user management handled by **Keycloak**, with automatic user profile syncing to the `user-service`.
-   **AI Calorie Calculator**: Provides personalized daily calorie targets (maintenance and goal-specific) based on user profile data, powered by Gemini.
-   **Activity Tracking**: Log workouts, duration, calories burned, and other metrics.
-   **AI Activity Suggestions**: Asynchronously provides AI-generated feedback and suggestions for logged activities.
-   **Nutritional Analysis**: Users can log the food they eat, and the AI service will return a detailed nutritional breakdown (calories, protein, carbs, fat).
-   **Goal & Progress Tracking**:
    -   Set and manage primary fitness goals (e.g., fat loss, muscle gain).
    -   Log daily weight.
    -   Track personal records (PRs) for various exercises.
    -   Upload weekly progress photos (integrates with AWS S3).
-   **Daily AI Summary**: Delivers a brief, personalized summary of the user's progress toward their goal.

---

## System Architecture
front end link : https://github.com/dayve-22/jackedLab-frontend.git
This project is built using a microservice architecture, promoting separation of concerns, scalability, and resilience.



[Image of a microservice architecture diagram]


The main components are:

-   **`api-gateway`**: The single entry point for all client requests. It handles request routing, rate limiting, and security. It also contains a custom filter to sync Keycloak users with the `user-service` on their first login.
-   **`eureka-server`**: The service discovery registry. All microservices register themselves here, allowing them to find and communicate with each other using service names.
-   **`config-server`**: Centralized configuration management for all microservices. (Not explicitly mentioned, but implied by a full microservice setup).
-   **`user-service`**: Manages all user data, including profiles (height, weight, age, goals) and authentication details (Keycloak ID).
-   **`activity-service`**: Responsible for logging user activities (workouts, food, daily weight, etc.).
-   **`ai-service`**: A dedicated service that acts as a client to the Google Gemini API. It handles all prompt engineering and AI-related business logic.
-   **`rabbitmq` (Message Broker)**: Used for asynchronous communication. For example, when an activity is logged, it's sent to a queue, and the `ai-service` consumes it later to generate suggestions without blocking the user.

---

## Technologies Used

-   **Backend**: Java (JDK 24+), Spring Boot 3
-   **Microservices**: Spring Cloud (Gateway, Eureka Client)
-   **Security**: Spring Security 6, Keycloak (OAuth2 & OIDC)
-   **AI**: Google Gemini API (via `google-ai-generativelanguage` SDK)
-   **Communication**:
    -   **Asynchronous**: RabbitMQ (Spring AMQP)
    -   **Synchronous**: Spring `WebClient` (Reactive)
-   **Database**: Spring Data JPA, PostgreSQL (or any relational DB)
-   **Build**: Apache Maven
-   **Utilities**: Lombok

---

## Getting Started

Follow these steps to get the project running on your local machine.

### Prerequisites

-   Java (JDK 24 or newer)
-   Apache Maven
-   Docker and Docker Compose (strongly recommended for external services)
-   A Google Gemini API Key
-   An AWS S3 account with credentials (for photo uploads)

### 1. External Services (Docker Compose)

The easiest way to run the required external services (Keycloak, PostgreSQL, RabbitMQ) is with Docker. Create a `docker-compose.yml` file in the root of your project:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: jackedlabs-postgres
    environment:
      - POSTGRES_USER=admin
      - POSTGRES_PASSWORD=admin
      - POSTGRES_DB=user_service_db
    ports:
      - '5432:5432'
    volumes:
      - postgres-data:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:3.12-management
    container_name: jackedlabs-rabbitmq
    ports:
      - '5672:5672' # AMQP port
      - '15672:15672' # Management UI
    environment:
      - RABBITMQ_DEFAULT_USER=guest
      - RABBITMQ_DEFAULT_PASS=guest

  keycloak:
    image: quay.io/keycloak/keycloak:24.0
    container_name: jackedlabs-keycloak
    command: start-dev
    environment:
      - KC_DB=postgres
      - KC_DB_URL=jdbc:postgresql://postgres:5432/keycloak_db
      - KC_DB_USERNAME=admin
      - KC_DB_PASSWORD=admin
      - KC_HTTP_ENABLED=true
      - KC_HTTP_PORT=8181 # Use 8181 to avoid conflicts
      - KEYCLOAK_ADMIN=admin
      - KEYCLOAK_ADMIN_PASSWORD=admin
    ports:
      - '8181:8181'
    depends_on:
      - postgres

volumes:
  postgres-data:

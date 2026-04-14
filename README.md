# SE310-SteamLikeApp

A Java-based application built with Maven, simulating a Steam-like platform.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven (or use the provided `mvnw` wrapper)

## Getting Started

1. Clone the repository.
2. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(Note: Adjust the run command based on your framework/packaging, e.g., Spring Boot or executable JAR)*

## Database

The project uses a SQLite database (`identifier.sqlite` / `steamdb.sqlite`) for local development and testing.

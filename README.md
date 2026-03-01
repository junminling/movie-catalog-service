# Movie Catalog Service

A Spring Boot application for managing a movie catalog.

## Technologies

- Java 17
- Spring Boot 4.0.3
- Maven 3.9.12 (via Maven Wrapper)

## Prerequisites

- Java 17 or higher
- No need to install Maven (project uses Maven Wrapper)

## Building the Project

This project uses a custom Maven settings file to download dependencies from Maven Central.

**Build the project:**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml clean package
```

**Run tests:**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml test
```

**Compile only:**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml compile
```

## Running the Application

```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml spring-boot:run
```

Or after building, run the jar directly:
```bash
java -jar target/movie-catalog-service-0.0.1-SNAPSHOT.jar
```

## Project Structure

```
movie-catalog-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/juneb/movie_catalog_service/
│   │   │       └── MovieCatalogServiceApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/
│           └── io/juneb/movie_catalog_service/
│               └── MovieCatalogServiceApplicationTests.java
├── pom.xml
└── README.md
```

## Configuration

The application configuration is in `src/main/resources/application.yaml`.

Default application name: `movie-catalog-service`

## Why the `-s` flag?

This project requires the `-s ~/git-juneb/.mvn/settings.xml` flag to use a custom Maven settings file that downloads dependencies from Maven Central instead of internal repositories.

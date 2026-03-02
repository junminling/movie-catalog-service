# Movie Catalog Service

A Spring Boot application for managing a movie catalog using OpenAPI/Swagger for API-first development.

## Technologies

- Java 17
- Spring Boot 4.0.3
- Maven 3.9.12 (via Maven Wrapper)
- OpenAPI Generator 7.10.0
- Swagger Annotations

## Prerequisites

- Java 17 or higher
- No need to install Maven (project uses Maven Wrapper)

## API Design

This project follows an **API-first approach** using OpenAPI 3.0 specification:
- API is defined in `src/main/resources/openapi.yaml`
- Models and REST API interfaces are auto-generated during build
- Controller implementations are written manually to implement the generated interfaces

## Building the Project

This project uses a custom Maven settings file to download dependencies from Maven Central.

**Build the project (includes OpenAPI code generation):**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml clean package
```

**Compile only (generates API code):**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml clean compile
```

**Generate OpenAPI code only:**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml generate-sources
```

**Run tests:**
```bash
./mvnw -s ~/git-juneb/.mvn/settings.xml test
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
│   │   │       ├── MovieCatalogServiceApplication.java
│   │   │       └── resource/
│   │   │           └── CatalogController.java (manual implementation)
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── openapi.yaml (API specification)
│   └── test/
│       └── java/
│           └── io/juneb/movie_catalog_service/
│               └── MovieCatalogServiceApplicationTests.java
├── target/
│   └── generated-sources/openapi/
│       └── src/main/java/io/juneb/movie_catalog_service/
│           ├── api/
│           │   └── CatalogApi.java (auto-generated)
│           └── model/
│               └── CatalogItem.java (auto-generated)
├── pom.xml
└── README.md
```

## Configuration

The application configuration is in `src/main/resources/application.yaml`.

Default application name: `movie-catalog-service`

## API Endpoints

All endpoints are defined in `openapi.yaml` and implemented in `CatalogController.java`:

- `GET /catalog/{userId}` - Get all catalog items for a user
- `POST /catalog/{userId}` - Create a new catalog item
- `GET /catalog/{userId}/{id}` - Get a specific catalog item by ID
- `PUT /catalog/{userId}/{id}` - Update a catalog item
- `DELETE /catalog/{userId}/{id}` - Delete a catalog item

### CatalogItem Model

```json
{
  "id": 1,
  "name": "Inception",
  "desc": "A mind-bending thriller about dreams",
  "rating": 9
}
```

## OpenAPI Code Generation

The OpenAPI Generator Maven Plugin automatically generates:
- **Model classes** from the `components/schemas` section
- **API interfaces** from the `paths` section

Generated code is placed in `target/generated-sources/openapi/` and is automatically included in the build classpath.

To modify the API:
1. Edit `src/main/resources/openapi.yaml`
2. Run `./mvnw -s ~/git-juneb/.mvn/settings.xml generate-sources`
3. Update controller implementations if needed

## Why the `-s` flag?

This project requires the `-s ~/git-juneb/.mvn/settings.xml` flag to use a custom Maven settings file that downloads dependencies from Maven Central instead of internal repositories.

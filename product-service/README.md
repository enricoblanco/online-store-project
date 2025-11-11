# Product Service

A simple REST API for managing products built with Spring Boot and MongoDB.

## Technologies

- Java 21
- Spring Boot 3.5.7
- MongoDB
- Maven
- Lombok

## Prerequisites

- JDK 21
- Maven
- MongoDB

## Running the Application

```bash
./mvnw spring-boot:run
```

## API Endpoints

- `POST /api/product` - Create a new product
- `GET /api/product` - Get all products

## API Documentation

OpenAPI documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

## Testing

Run tests with:
```bash
./mvnw test
```

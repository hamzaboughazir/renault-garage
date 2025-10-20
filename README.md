# Renault Garage (Java 21, Spring Boot 3, H2, Kafka,Docker compose Swagger, DDD + hexagonal )

- **Packages**: base `fr.renault`
- **DDD layers**:
  - `domain`: models and repository ports
  - `application`: use cases (services) + publisher port
  - `infrastructure`: JPA persistence, Kafka producer/consumer
  - `interfaces`: REST controllers, DTOs, mappers, exception handler
- **DB**: H2 in-memory
- **API Docs**: http://localhost:8080/swagger-ui.html

## Run
```bash
./mvnw spring-boot:run
```
Or with Maven installed:
```bash
mvn spring-boot:run
```
```Run( gestion des conteneurs Docker )``` pour démarrer les services nécessaires (Kafka, Zookeeper) :
 docker-compose up -d

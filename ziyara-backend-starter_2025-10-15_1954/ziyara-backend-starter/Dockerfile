# ===============================
# 1️⃣ BUILD STAGE
# ===============================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Create app directory
WORKDIR /app

# Copy pom.xml and download dependencies first (to leverage Docker cache)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the Spring Boot app
RUN mvn -B clean package -DskipTests

# ===============================
# 2️⃣ RUNTIME STAGE
# ===============================
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy only the built jar from previous stage
COPY --from=builder /app/target/ziyara-backend-0.0.1-SNAPSHOT.jar app.jar

# Environment variables for database (override with docker-compose or env)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/ziyara_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8080

# Expose application port
EXPOSE 8080

# Default command
ENTRYPOINT ["java", "-jar", "app.jar"]

# Dockerfile for Spring Boot with Maven
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

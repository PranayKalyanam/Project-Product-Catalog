# STAGE 1: Build the JAR file
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy dependency files first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the final JAR
COPY src ./src
# -DskipTests ensures the Docker build doesn't run tests again
RUN mvn clean install -DskipTests

# STAGE 2: Create the final lightweight production image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Expose the application port
EXPOSE 8080

# The JAR file is named product-catalog.jar based on your pom.xml finalName
COPY --from=build /app/target/product-catalog.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
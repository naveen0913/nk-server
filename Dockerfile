# Use an official JDK runtime as a parent image
#FROM eclipse-temurin:17-jdk-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy Maven wrapper and config
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#
## Give execute permission to mvnw
#RUN chmod +x mvnw
#
## Copy source code
#COPY src src
#
#RUN ./mvnw clean package -DskipTests
#
#EXPOSE 8083
#
## Run the jar
#CMD ["java", "-jar", "target/sample-0.0.1-SNAPSHOT.jar"]

# Use the Maven image with JDK 17 for the build stage
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw clean package

# Use a lightweight JDK 17 image for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar buyogo.jar

# Run the application
ENTRYPOINT ["java", "-jar", "sample-0.0.1-SNAPSHOT.jar"]

# Expose port 8080 (adjust if necessary)
EXPOSE 8083

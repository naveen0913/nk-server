# ---------- Build Stage ----------
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven files first (for caching dependencies)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (faster builds on re-deploys)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for Docker builds)
RUN ./mvnw clean package -DskipTests


# ---------- Runtime Stage ----------
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy only the built JAR from build stage
COPY --from=build /app/target/sample-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 8083

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]


## Use the Maven image with JDK 17 for the build stage
#FROM maven:3.8.5-openjdk-17 AS build
#
## Set the working directory
#WORKDIR /app
#
## Copy Maven files and source code
#COPY pom.xml .
#COPY src ./src
#
## Build the application
#RUN ./mvnw clean package
#
## Use a lightweight JDK 17 image for the runtime stage
#FROM openjdk:17-jdk-slim
#
## Set the working directory
#WORKDIR /app
#
## Copy the JAR file from the build stage
#COPY --from=build /app/target/*.jar buyogo.jar
#
## Run the application
#ENTRYPOINT ["java", "-jar", "sample-0.0.1-SNAPSHOT.jar"]
#
## Expose port 8080 (adjust if necessary)
#EXPOSE 8083

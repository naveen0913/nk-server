# Use an official JDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Copy source code
COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8083

# Run the jar
CMD ["java", "-jar", "target/sample-0.0.1-SNAPSHOT.jar"]

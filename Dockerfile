
# Use an official JDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven build file and source code
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8083

# Run the jar file
CMD ["java", "-jar", "target/sample-0.0.1-SNAPSHOT.jar"]

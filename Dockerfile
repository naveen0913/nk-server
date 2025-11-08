## ---------- Build Stage ----------
#FROM maven:3.8.5-openjdk-17 AS build
#
## Set working directory
#WORKDIR /app
#
## Copy Maven files first (for caching dependencies)
#COPY pom.xml .
#COPY mvnw .
#COPY .mvn .mvn
#
## Give execute permission to mvnw
#RUN chmod +x mvnw
#
## Download dependencies (faster builds on re-deploys)
#RUN ./mvnw dependency:go-offline -B
#
## Copy source code
#COPY src src
#
## Build the application (skip tests for Docker builds)
#RUN ./mvnw clean package -DskipTests
#
## Set working directory
#WORKDIR /app
#
## Copy only the built JAR from build stage
#COPY --from=build /app/target/sample-0.0.1-SNAPSHOT.jar app.jar
#
## Expose app port
#EXPOSE 8083
#
## Run the jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:17

WORKDIR /app

COPY ./target/sample-0.0.1-SNAPSHOT.jar /app

EXPOSE 8083

CMD ["java","-jar","sample-0.0.1-SNAPSHOT.jar"]


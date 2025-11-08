#FROM openjdk:17
#WORKDIR /app
#COPY ./target/sample-0.0.1-SNAPSHOT.jar /app
#EXPOSE 8083
#CMD ["java","-jar","sample-0.0.1-SNAPSHOT.jar"]

FROM amazoncorretto:17
WORKDIR /app
COPY ./target/sample-0.0.1-SNAPSHOT.jar /app
EXPOSE 8083
CMD ["java", "-jar", "sample-0.0.1-SNAPSHOT.jar"]

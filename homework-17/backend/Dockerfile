FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/Homework-10-AJAX-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
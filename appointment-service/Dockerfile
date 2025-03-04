FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY --from=build /app/appointment-service/target/*.jar /app/appointments.jar

ENTRYPOINT ["java","-jar", "/app/appointments.jar"]
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY --from=build /app/doctor-service/target/*.jar /app/doctors.jar

ENTRYPOINT ["java", "-jar", "/app/doctors.jar"]
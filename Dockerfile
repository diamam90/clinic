FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY --from=build /app/client-service/target/*.jar /app/clients.jar
COPY --from=build /app/doctor-service/target/*.jar /app/doctors.jar
COPY --from=build /app/appointment-service/target/*.jar /app/appointments.jar

EXPOSE 8081
EXPOSE 8082
EXPOSE 8083

ENTRYPOINT java -jar /app/clients.jar & java -jar /app/doctors.jar & java -jar /app/appointments.jar
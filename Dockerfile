FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mkdir -p src && mv main src/main

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/loja-games-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-cp", "app.jar", "org.example.Main"]
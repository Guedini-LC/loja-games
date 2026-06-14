FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY main ./src/main

RUN mkdir -p src && mv main src/main

RUN mvn -DskipTests package

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/loja-games-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-cp", "app.jar", "org.example.Main"]
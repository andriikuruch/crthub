FROM maven:3.9.12-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml ./
RUN mvn -B -e -C dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipsTests

FROM eclipse-temurin:21.0.10_7-jre-alpine-3.23
WORKDIR /app
COPY --from=builder /build/target/certhub-0.0.1-SNAPSHOT.jar certhub-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "certhub-0.0.1-SNAPSHOT.jar"]

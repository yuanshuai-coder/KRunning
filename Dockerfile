# syntax=docker/dockerfile:1

FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY server/pom.xml server/pom.xml
COPY server/src server/src
RUN mvn -f server/pom.xml -B -DskipTests package

FROM eclipse-temurin:17-jre AS runtime
ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=Asia/Shanghai
WORKDIR /app
COPY --from=builder /build/server/target/run-tracker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN ./gradlew --version || true

COPY src src
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

VOLUME /data

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app/app.jar"]

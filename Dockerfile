FROM gradle:7.6.2-jdk17 AS build

WORKDIR /app
COPY --chown=gradle:gradle build.gradle settings.gradle gradle.properties ./
COPY --chown=gradle:gradle gradle ./gradle
RUN gradle dependencies --no-daemon || true
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
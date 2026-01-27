# Шаг 1: Сборка проекта
FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Собираем Fat JAR
RUN gradle buildFatJar --no-daemon

# Шаг 2: Запуск
# Используем стабильный образ Temurin вместо удаленного openjdk
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-app.jar
ENTRYPOINT ["java", "-jar", "/app/ktor-app.jar"]
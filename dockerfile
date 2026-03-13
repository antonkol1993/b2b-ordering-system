# Используем JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Maven файлы сначала для кэширования зависимостей
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Устанавливаем права на скрипт mvnw
RUN chmod +x mvnw

# Загружаем зависимости
RUN ./mvnw dependency:go-offline

# Копируем исходники
COPY src ./src

# Собираем приложение (без тестов, чтобы быстрее)
RUN ./mvnw clean package -DskipTests

# Открываем порт 8080
EXPOSE 8080

# Команда запуска приложения
CMD ["java", "-jar", "target/b2b-system-0.0.1-SNAPSHOT.jar"]
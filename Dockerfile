# Use JDK 22 to build the JAR
FROM openjdk:22-jdk-slim AS build

WORKDIR /app

# Copy Gradle files first (for caching)
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle gradle
RUN chmod +x gradlew

# Copy the source code
COPY . .

# Build the JAR
RUN ./gradlew clean bootJar

# Rename and move the JAR
RUN mv build/libs/ToDoApplication-0.0.1-SNAPSHOT.jar app.jar

# Use a smaller runtime image
FROM openjdk:22-jdk-slim

WORKDIR /app
COPY --from=build /app/app.jar app.jar

CMD ["java", "-jar", "app.jar"]
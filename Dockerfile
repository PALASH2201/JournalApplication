# First stage: Build the JAR
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY journalApp /app
RUN mvn clean package -DskipTests

# Second stage: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/journalApp-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]

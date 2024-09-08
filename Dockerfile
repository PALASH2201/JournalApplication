# Use an official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the journalApp/target directory to the /app directory in the container
COPY journalApp/target/journalApp-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the default port for Spring Boot (usually 8080)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]

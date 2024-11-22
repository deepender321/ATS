# Use a base image that has Java (OpenJDK 17) installed
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from your build directory (you'll update this after your Maven build)
COPY target/Applicationservices.jar /app/Applicationservices.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/Applicationservices.jar"]

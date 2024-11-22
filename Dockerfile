FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the .jar file from logs to the container
COPY logs/Applicationservices.jar /app/Applicationservices.jar

# Run the .jar file
CMD ["java", "-jar", "/app/Applicationservices.jar"]

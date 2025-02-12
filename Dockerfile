# Use Amazon ECR Maven and Amazon Corretto (Java 17) images to avoid Docker Hub rate limits
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-alpine AS build

# Install Maven
FROM public.ecr.aws/amazonlinux/amazonlinux:2 AS maven
RUN yum install -y java-17-amazon-corretto maven

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the source code to the container
COPY pom.xml /app
COPY src /app/src

# Build the .jar file using Maven
RUN mvn clean package -DskipTests

# Use a lightweight JRE image to run the application
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-alpine

# Set the working directory
WORKDIR /app

# Copy the .jar file from the build stage to the current working directory
COPY --from=build /app/target/ApplicationServices-0.0.1-SNAPSHOT.jar /app/ApplicationServices-0.0.1-SNAPSHOT.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/ApplicationServices-0.0.1-SNAPSHOT.jar"]

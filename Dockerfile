# Use the official OpenJDK 17.0.9 base image
FROM adoptopenjdk:17.0.9-jdk-hotspot

# Refer to Maven build 
ARG JAR_FILE=target/docker-spring-boot-1.0.jar

# Set the working directory to /opt/app
WORKDIR /opt/app

# Copy the JAR file into the container at /opt/app/docker-spring-boot-1.0.jar
COPY ${JAR_FILE} docker-spring-boot-1.0.jar

# Specify the command to run on container start
CMD ["java", "-jar", "docker-spring-boot-1.0.jar"]


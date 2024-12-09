# using maven to build springboot app
FROM maven:3.8.4-openjdk-17 AS builder

# set working directory
WORKDIR /linuxlab

# cop pom.xml and source code
COPY pom.xml .  
COPY src ./src

# build the application
RUN mvn clean package -DskipTests

# create  final image
FROM openjdk:17-jdk-slim

# setting working directory
WORKDIR /linuxlab

# copying the built JAR from builder stage
COPY --from=builder /linuxlab/target/*.jar /linuxlab/Linux-Labyrinth.jar


# exposing port 8080 - localhost
EXPOSE 8080

# run the application
ENTRYPOINT ["java", "-jar", "Linux-Labyrinth.jar"]
#!/bin/bash

# Step 1: Build the React app
echo "Building React app..."
cd react
npm install    # Install dependencies if not already installed
npm run build  # Build the React app

# Step 2: Copy the React build output to the Spring Boot static folder
echo "Copying React build to Spring Boot static folder..."
cp -r build/* ../Linux-Labyrinth/src/main/resources/static/
cp -r ../Linux-Labyrinth/src/main/resources/static/index.html ../Linux-Labyrinth/src/main/resources/templates/


# Step 3: Build the Spring Boot app
echo "Building Spring Boot app..."
cd ../Linux-Labyrinth  # Go to the Spring Boot project directory
mvn clean install  # Build the Spring Boot app with Maven

# Step 4: Run the Spring Boot app
echo "Running Spring Boot app..."
mvn spring-boot:run  # Run the Spring Boot application

echo "Build and deployment complete!"
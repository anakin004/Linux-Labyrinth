#!/bin/bash

#building react
echo "Building React app..."
cd react
npm install    # Install dependencies if not already installed
npm run build  # Build the React app

# copying react build into springboot static
# -- Also copying index.html into template folder for Spring Boot
echo "Copying React build to Spring Boot static folder..."
cp -r build/* ../Linux-Labyrinth/src/main/resources/static/
cp -r ../Linux-Labyrinth/src/main/resources/static/index.html ../Linux-Labyrinth/src/main/resources/templates/


# building springboot
echo "Building Spring Boot app..."
cd ../Linux-Labyrinth  
mvn clean install

# running springboot
echo "Running Spring Boot app..."
mvn spring-boot:run 

echo "Build and deployment complete!"

# Step 1: Build the React app
echo "Building React app..."
cd react
npm install    # Install dependencies if not already installed
npm run build  # Build the React app

# Step 2: Copy the React build output to the Spring Boot static folder
echo "Copying React build to Spring Boot static folder..."
cp -r build/* ../Linux-Labyrinth/src/main/resources/static/

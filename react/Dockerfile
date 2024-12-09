# Build stage
FROM node:18-alpine AS builder

# setting working directory for build stage
WORKDIR /linuxlab

# cpy package files for dependency installation
COPY package*.json ./

RUN npm install

# copying all project files
COPY . .

# building the React application
RUN npm run build

# production stage
FROM node:18-alpine

# set working directory for production stage
WORKDIR /linuxlab

# cpy only the build output and package files
COPY --from=builder /linuxlab/build ./build
COPY --from=builder /linuxlab/public ./public
COPY package*.json ./


# install only production dependencies
# doing run nmp install was not working, saw this with claude :)
RUN npm ci --only=production

# exposing port 3000 for React application
EXPOSE 3000

# start the application
RUN npm install -g serve
CMD ["serve", "-s", "build", "-l", "3000"]
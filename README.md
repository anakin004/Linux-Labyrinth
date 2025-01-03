# Linux Labyrinth

## Project Overview
**Linux Labyrinth** is a Spring Boot-based web application designed to explore and demonstrate the integration of a PostgreSQL database with a RESTful API along with a fun an interractive game to learn about Linux/Unix-like commands. This project shows how to leverage Spring Boot to rapidly build a backend application that can interact with a PostgreSQL database. The application uses Java 17, Spring Boot, and PostgreSQL for data management, along with a Maven-based build system.

## Features
- **PostgreSQL Database Integration**: The application uses PostgreSQL as the database to store and retrieve application data, such as user data or any other relevant application information.
- **Spring Boot Framework**: The project leverages Spring Boot for rapid development and deployment of the application. It provides features like automatic configuration, embedded Tomcat server, and Spring Data JPA for database access.
- **RESTful API**: The application exposes endpoints to interact with the data stored in the PostgreSQL database, allowing users to perform CRUD (Create, Read, Update, Delete) operations via HTTP requests. I introduced API rate-limiting and distribution for access-control and security
- **Docker Support**: The project can be configured to run PostgreSQL in a Docker container, making it easier to set up a local development environment without the need for manual PostgreSQL installation.
- **UI**: An interractive web interface that makes the learning process engaging.
- **Web-Hosting**: I hosted the web application on Azure using a Cloud-Based Virtual Machine (VM) to provide reliable and scalable access via GitHub. This enabled efficient deployment and streamlined access for users.
## Technologies Used
- **Java 17, HTML & CSS, JavaScript**: Programming languages used for this project.
- **Tailwind CSS, React, PostCSS**: Frontend technologies used for styling, building dynamic user interfaces, and optimizing CSS.
- **Spring Boot**: A Java framework used to create the backend API and manage the application’s configuration, security, and database interactions.
- **PostgreSQL**: A powerful relational database management system used to store application data.
- **Maven**: The build tool used for managing dependencies, compiling, testing, and running the application.
- **Docker**: Used optionally for setting up PostgreSQL in a containerized environment, making the setup process easier.
- **Azure**: A cloud computing platform that offers a variety of services to help users build, run, and manage applications
- **Jenkins**: an open-source automation server that helps automate software development tasks (Used for CI/CD)
## Important Notes

- If you want to replicate this project and impliment it, be wary
- Since the users are inputting shell commands, no matter how much error checking there is it will (kind of) never be 100% secure
- Note on docker container, it is included in this project, I need to copy over game logic and some path resolving. Everything else is fully functional though, React and Spring Boot are both building and running, along with psql, you can create instances of users in the database via JPA entity automatically created by hibernate as in Spring Boot, spring.jpa.hibernate.ddl-auto is set to update, this allows Hibernate to automatically create missing tables and/or columns based on Entity classes :)


## Setup Instructions

### Clone the Repository
Start by cloning the repository to your local machine:
```bash
git clone https://github.com/anakin004/Linux-Labyrinth.git
cd Linux-Labyrinth
```


## Demos 
- ** If you don't want spoilers don't watch demo <<

https://github.com/user-attachments/assets/1298929b-03f9-4b7b-b2bd-5b35caaa9427


![Screenshot 2024-12-09 004406](https://github.com/user-attachments/assets/ab527795-5e15-45d6-a6fc-87d07af1635b)

-- Recent demo with api key integration, same game logic
-- you simply input your api that is generated when you make one for the first time
-- and your allowed to use the command line interface, to enfore rest-api design
![Capture](https://github.com/user-attachments/assets/4df7420e-0253-4691-b2d5-4e502df399cc)

-- Jenkins Addition
![image](https://github.com/user-attachments/assets/c4a87816-eda3-46e1-9466-6d8c356082fd)









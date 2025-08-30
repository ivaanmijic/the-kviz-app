# The Kviz App - Real-Time Quiz Application
Razvoj web aplikacija 2024./25. Završni projekt

## About The Project

**The Kviz App** is an interactive, real-time multiplayer web application for live quizzes. It was developed as the final project for the "Web Application Development" course at the Faculty of Electrical Enginnering, University of Tuzla. The application allows for quiz creation, game management, and real-time competiotion between players.

This project demonstrates the use of modern and conventional web technologies to build dynamic and interactive applications, with a strong emphasis on WebSocket communication for a seamless real-time experience.

## The Team
- **Name:** Ivan Mijić  
  **Email:** imijic02@gmail.com  
  **LinkedIn:** [Ivan Mijić](https://www.linkedin.com/in/ivan-miji%C4%87-4984612b8/)  

- **Name:** Haris Mujić   
  **Email:** mujic.haris02@gmail.com  
  **LinkedIn:** [Haris Mujić](https://www.linkedin.com/in/haris-muji%C4%87-a937a219b/)

## Technologies used
The application was built using the following technology stack:
- **Backend**:
    - Java
    - Java Servlets
    - WebSocket for real-time communication
    - JPA / Hibernate for object-relational mapping
    - MySQL as the database
- **Frontend**:
    - JavaServer Pages (JSP)
    - HTML5
    - Tailwind CSS for modern and responsive styling
    - Shoelace Style for modern UI components
    - JavaScript and TypeScript for client-side logic and interactivity
- **Build & Utility Tools**:
    - Gradle for project and dependency management
    - SheetJS (XLSX) for generating and downloading Excel files

## Features
The application is divided into two main parts: an admin interface for creating and managing quizzes, and a player interface for participating in the game.

### Admin Features
- **Quiz Management**: Admins can create new quizzes, and add, edit, or delete questions and their corresponding answers.
- **Game Initiation**: An admin can start a quiz session, which generates a unique game code for players to join.
- **Real-Time Game Monitoring**: View the number of connected players and track the quiz's progress live.
- **Leaderboard Display**: After each question and at the end of the quiz, the admin can view the player rankings.
- **Download Results**: The final leaderboard can be downloaded as an `.xlsx` file for record-keeping.

### Players Features
- **Join a Game**: Players can join a game session by entering the unique game code and a nickname.
- **Real-Time Gameplaye**: Questions are displayed to all players simultaneously, with a countdown timer for submitting an answer.
- **Interactive UI**: A modern and intuitive user interface with clear visual cues to enhance the gameplaye experience.
- **Live Leaderboard**: After ansering a question, players receive feedback of the anwers's correctness and can view the updated leaderboard.
- **Final Standings**: At the end of the quiz, a final leaderboard is displayed, higlighting the top three players.

## Instalation and Setup
To run this project locally, please follow these steps:

**Prerequirements**:
- **Java 21** (or later)
    Required to build and rung the Jakarta EE / Hibernate backend.
    You can check your version with:
    ```bash
    java -version
    ```

- **Gradle 8.x** (recommended)
    Used as the main build tool. If you don't want to install it globally, the project includes the Gradle Wrapper (`./gradlew`) which will download the correct version automatically.
    ```bash
    gradle -v
    ```
- **Node.js 20.x** and **npm 9.x**
    Required for building and bundling frondend dependencies (Shoelace, Typescript).
    ```bash
    node -v
    npm -v
    ```

- **MySQL 8.x**
    Required as the database for the application.
    You must create the database and a user with the following credentials before running the app:
    ```sql
    CREATE DATABASE kvizdb;

    CREATE USER 'superadmin'@'localhost' IDENTIFIED BY 'admin123';

    GRANT ALL PRIVILEGES ON kvizdb.* TO 'superadmin'@'localhost';

    FLUSH PRIVILEGES;
    ```

Optional but recommended:
- **Git** - for cloning the repository
- **IDE** such as Intellij IDEA, Eclipse, or VS Code with Java * Typescript support.

**Steps**:
1. **Clone the repository**:
    ```bash
    git clone https://github.com/ivaanmijic/the-kviz-app.git
    cd the-kviz-app
    ```

2. **Run the backend (Tomcat server)**:
    ```bash
    gradle appRun
    ```

3. **Accss the Application**:
- Open your web browser and navigate to `http://localhost:8080`
- To create and manage a quiz, access the admin panel.
- To play, start a game from the admin panel and use the generated code to join from the main page.

## Screenshots

## Demo Videos

For a better insight into the functionality of the application, three demo videos are available:

1. 🎥 [Admin (management) part](https://drive.google.com/drive/folders/1WwG9SnUnDbRn5m6tWoHtdoyBchNZUhyj) 
*Creating, editing and managing quizzes.*

2. 🎥 [Remember me cookie](https://drive.google.com/drive/folders/1WwG9SnUnDbRn5m6tWoHtdoyBchNZUhyj) 
*Demonstration of permanent user login via cookies.*

3. 🎥 [Play part of the application](https://drive.google.com/drive/folders/1WwG9SnUnDbRn5m6tWoHtdoyBchNZUhyj) 
*Showing the flow of the game and how to play the quiz.*

---
---

# Lazy---Meal---Picker
 ## Project Description


This project is a meal planning web application build with Java, and JavaScript. The application provides a simple web interface to filter and explore different meals based on users preference .

The application uses the built-in HTTP server of Java to process requests, and return meal data. No database is needed for storing meal data in memory with the help of java arrays.

The project shows:

*Java back-end development
*Frontend HTML/CSS/JavaScript integration
*Handling HTTP Requests
*Dynamic content creation
*Browser-side data persistence using local storage

--- 

## Features 
* Filters meals by category (Breakfast, Lunch, Snack)
* Filter  by Maximum preparation time
* Filter by max calories
* Filter based on user preference of a no vegetable based meal
* Surprize Me button to randomly suggest a meal without any users specifications
* Meal of the day that changes daily
* Save meals to favorites
* Meal history showing last 8 meals
* Copy meal to clipboard
* Star ratin g system
* A counter for picks, favorites, history, and ratings
* A dark mood toggle
---
### Technologies used
* Java 
* HTML 
* Cascading Style Sheets (CSS)
*Java Script
* Java HTTP Server (com.sun.net.httpserver.HttpServer)
--- 

## Requirements

Before running the project you need to have:
* Java JDK 17 or higher
* IntelliJ IDEA / VS Code
* A web browser (Chrome, Edge, Firefox, etc.)


--- 

## Project Structure 
 project-folder/
| Application.java (backend)
|__ index.html (frontend)
|__ style.css  (frontend)
|__ script.js  (frontend)

--- 

## Running the project

1. Open the project in VS Code/ IntelliJ Idea.

2. Compile and run the Java application.
javac Application.java
java Application 

3. Launch your browser and go to:
http://localhost:8080/meal
4.Open index.html in your browser to use the frontend interface

--- 

## Data Base

In this project, we did not use any database.

During run-time the meal data are stored directly in Java arrays.
Users data such as favorites, history, and stats are saved in the browser's localStorage
--- 

## Future work
* Add a real database (MySQL/SQLite)
* Implement a user login system
* Refined UI/UX design
* Add meal images
  
--- 

## Author information

Arwa Abo Niaaj
May 7,2026



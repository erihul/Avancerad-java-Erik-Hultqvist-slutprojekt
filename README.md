###### Avancerad-java-Erik-Hultqvist-slutprojekt
###### Erik Hultqvist

# To-Do List

---
## Table of Contents
1. <ins>[Description of Project](#1-description-of-project)<ins/>
2. <ins>[Running the Application](#2-running-the-application)<ins/>
3. <ins>[Architecture oversight](#3-architecture-oversight)<ins/>
4. <ins>[API Operation Examples](#4-api-operation-examples)<ins/>
---

### 1. Description of Project
This Project is a **To-Do List** application that you run in the console where you can __*add*__, __*delete*__ and __*change*__ **Tasks** to and from a list. 

---
### 2. Running the Application
To run the application you can use an IDEA like; IntelliJ or VisualStudioCode and follow these steps:
1. Open the **ToDoApi** folder and run the __*ToDoApiApplication.java*__
2. Then open the **ToDoFrontEnd** folder and run __*HelloApplication.java*__
Now the application can be used.

---
### 3. Architecture oversight
##### Backend
- The Api-Server runs on port:8090, and is initialized with the **Spring Boot** framework.   

- __*TaskController*__ contains CRUD-operations(API endpoints) for user to reach the server.

- Then there is a Task-class in __*Task.java*__ that holds the Title and Description.

- Interface

##### Frontend

- In frontend the __*HelloController.java*__ handles the code for how the client/user deals with the backend(server) side.

- In __*To-Do.fxml*__ the **JavaFX** code is written and fine-tuned after hhe GUI has been built up in the program **SceneBuilder**.

-

### 4. API Operation Examples

- GET

    Call for the server(arraylist) where the tasks are gathered


- POST

    Creates a new task and saves it in server(arraylist)


- PUT

    Updates an existing task from server(arraylist)


- DELETE

    Deletes the task of choice from server(arraylist)
    
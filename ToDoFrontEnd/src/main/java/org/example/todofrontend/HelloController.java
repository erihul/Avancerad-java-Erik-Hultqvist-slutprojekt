package org.example.todofrontend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    void initialize() {
        // Run the getAllBooks method when the application starts
        getAllTasks();
        //OnClick effect
        DropShadow shadow = new DropShadow();
        addButton.setEffect(shadow);
        addButton.setOnMousePressed(e -> addButton.setEffect(null));
        addButton.setOnMouseReleased(e -> addButton.setEffect(shadow));
        changeButton.setEffect(shadow);
        changeButton.setOnMousePressed(e -> changeButton.setEffect(null));
        changeButton.setOnMouseReleased(e -> changeButton.setEffect(shadow));
        deleteButton.setEffect(shadow);
        deleteButton.setOnMousePressed(e -> deleteButton.setEffect(null));
        deleteButton.setOnMouseReleased(e -> deleteButton.setEffect(shadow));
        // Hoover over button effect
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #89e289;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #77c277;"));
        changeButton.setOnMouseEntered(e -> changeButton.setStyle("-fx-background-color: #f4c8d0;"));
        changeButton.setOnMouseExited(e -> changeButton.setStyle("-fx-background-color: #fdbec9;"));
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #fbb048;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #fb9c20;"));
    }

    @FXML
    private Button addButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField inputTitle;

    @FXML
    private TextArea inputDescription;

    @FXML
    private TextArea taskArea;

    @FXML
    void getAllTasks() {
        try{
            URL url = new URL("http://localhost:8090/api/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // Read the response from the server
            String response = readResponse(connection);

            // Parse the response into a JsonNode
            ObjectMapper prettyMapper = new ObjectMapper();
            JsonNode rootNode = prettyMapper.readTree(response);

            // Initialize a StringBuilder to append all task titles and descriptions
            StringBuilder tasksResponse = new StringBuilder();

            for (JsonNode taskNode : rootNode) {
                // Extract title and description for each task
                String taskTitleResponse = taskNode.has("title") ? taskNode.get("title").asText() :
                        "No title found";
                String taskDescriptionResponse = taskNode.has("description") ? taskNode.get("description")
                        .asText() : "No description found";
                // Append the task details to the StringBuilder
                tasksResponse.append(" - ").append(taskTitleResponse).append("\n")
                        .append(taskDescriptionResponse).append("\n\n");
            }
            // Set the accumulated task details in the taskArea
            taskArea.setText(tasksResponse.toString());
        }catch (Exception e){
            taskArea.setText("Error" + e.getMessage());
        }
    }
    @FXML
    void addNewTask(ActionEvent event) {

        if(!inputTitle.getText().isEmpty() && !inputDescription.getText().isEmpty()) {
            try {
                String taskTitle = inputTitle.getText();
                String taskDescription = inputDescription.getText();
                Task myTask = new Task(taskTitle, taskDescription);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(myTask);

                URL url = new URL("http://localhost:8090/api/tasks");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }
                String response = readResponse(connection);
            } catch (Exception e) {
                taskArea.setText("Error: " + e.getMessage());
                e.printStackTrace();
            }
            inputTitle.clear();
            inputDescription.clear();
            getAllTasks();
        }
    }
   @FXML
    void deleteTask(ActionEvent event) {
        try {
            String rawTitle = inputTitle.getText();
            String title = URLEncoder.encode(rawTitle, StandardCharsets.UTF_8.toString());


            //DeleteTask deleteTask = new DeleteTask(title);
            URL url = new URL("http://localhost:8090/api/tasks/"  + title);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(title);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input);
                os.flush();
            }
            String response = readResponse(connection);
        }catch (Exception e){
            taskArea.setText("Delete-Error: " + e.getMessage());
            e.printStackTrace();
        }
        inputTitle.clear();
        inputDescription.clear();
        getAllTasks();
    }
    @FXML
    void changeTask(ActionEvent event) {
        try {
            String rawTitle = inputTitle.getText();
            String title = URLEncoder.encode(rawTitle, StandardCharsets.UTF_8);
            String rawDescription = inputDescription.getText();
            String description = URLEncoder.encode(rawDescription, StandardCharsets.UTF_8);

            // Create a Task object with the updated title and description
            Task updatedTask = new Task(title, description);

            URL url = new URL("http://localhost:8090/api/tasks/"  + title);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(updatedTask);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0 , input.length);
                os.flush();
            }
            String response = readResponse(connection);
        }catch (Exception e){
            taskArea.setText("Delete-Error: " + e.getMessage());
            e.printStackTrace();
        }
        inputTitle.clear();
        inputDescription.clear();
        getAllTasks();
    }
    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}


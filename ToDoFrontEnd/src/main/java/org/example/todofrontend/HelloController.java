package org.example.todofrontend;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HelloController {

    @FXML
    private TextField inputTitle;

    @FXML
    private TextArea inputDescription;

    @FXML
    private TextArea taskArea;

   /* @FXML
    void addNewTask(ActionEvent event) {
        taskPane.addEventHandler();
    }*/
    @FXML
    void addNewTask(ActionEvent event) {
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
            taskArea.setText(response);
        } catch (Exception e) {
            taskArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
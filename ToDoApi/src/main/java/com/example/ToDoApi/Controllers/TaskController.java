package com.example.ToDoApi.Controllers;

import com.example.ToDoApi.Interface.TaskService;
import com.example.ToDoApi.Models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")



public class TaskController implements TaskService {
    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
    tasks.add(new Task("Träna", "Jogga 6km i Pildammarna"));
    tasks.add(new Task("Examination", "Börja med sista uppgiften i kursen Avancerad JAVA"));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    @PostMapping
    public ResponseEntity <List<Task>> addTask(@RequestBody Task task) {
        if(task != null) {
            tasks.add(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    @PutMapping("/{title}")
    public ResponseEntity <List<Task>> updateTask(@PathVariable String title, @RequestBody Task updatedTask) {
        boolean changed = false;

        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getTitle().equalsIgnoreCase(title)) {
                tasks.get(i).setTitle(URLDecoder.decode(updatedTask.getTitle(), StandardCharsets.UTF_8));
                tasks.get(i).setDescription(URLDecoder.decode(updatedTask.getDescription(),StandardCharsets.UTF_8));
                changed = true;
                break;
            }
        }
        if(changed) {
            return ResponseEntity.status(HttpStatus.OK).body(tasks);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Override
    @DeleteMapping("/{title}")
    public ResponseEntity <List<Task>> deleteTask(@PathVariable String title) {
        boolean removed = false;

        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getTitle().equalsIgnoreCase(title)) {
                tasks.remove(i);
                removed = true;
            }
        }
        if(removed) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
            //return ResponseEntity.ok(title + " has been deleted from your To-Do list");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

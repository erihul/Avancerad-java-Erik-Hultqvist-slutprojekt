package com.example.ToDoApi.Controllers;

import com.example.ToDoApi.Models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tasks")

public class TaskController {
    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
    tasks.add(new Task("Träna", "Jogga 6km i Pildammarna"));
    tasks.add(new Task("Examination", "Börja med sista uppgiften i kursen Avancerad JAVA"));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        if(task != null) {
            tasks.add(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping("/{title}")
    public ResponseEntity<Task> updateTask(@PathVariable String title, @RequestBody Task updatedTask) {
        for(Task task : tasks){
            if(task.getTitle().equalsIgnoreCase(title)) {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteTask(@PathVariable String title) {
        boolean removed = false;

        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getTitle().equalsIgnoreCase(title)) {
                tasks.remove(i);
                removed = true;
            }
        }
        if(removed) {
            return ResponseEntity.ok(title + " has been deleted from your To-Do list");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("To-Do assignment does not exist");
        }
    }
}

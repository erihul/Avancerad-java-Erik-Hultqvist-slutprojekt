    package com.example.ToDoApi.Interface;

    import com.example.ToDoApi.Models.Task;
    import org.springframework.http.ResponseEntity;

    import java.util.List;

    public interface TaskService {
        List<Task> getAllTasks();
        ResponseEntity<List<Task>> addTask(Task task);
        ResponseEntity<List<Task>> updateTask(String title, Task updatedTask);
        ResponseEntity<List<Task>> deleteTask(String title);
    }

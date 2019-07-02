package com.system.taskmanagement.rest;

import com.system.taskmanagement.domain.model.Task;
import com.system.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    //get all tasks
    @GetMapping("/tasks")
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    //create a task
    @PostMapping("/tasks")
    public Task addBook(@RequestBody Task task) {
        return taskService.addBook(task);
    }

    //change a task
    @PutMapping("/tasks/{taskId}")
    public Task updateTask(@PathVariable Long taskId, @RequestBody Task taskRequest) {
        return taskService.updateTask(taskId, taskRequest);
    }

    //complete a task
    @PatchMapping("/tasks/{taskId}")
    public Task completeTask (@PathVariable Long taskId){
        return taskService.completeTask(taskId);
    }

    //delete a task
    @DeleteMapping("/tasks/{taskId}")
    public Task deleteTask (@PathVariable Long taskId){
        return taskService.deleteTask(taskId);
    }
}

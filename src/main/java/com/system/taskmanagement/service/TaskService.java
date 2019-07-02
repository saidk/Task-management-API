package com.system.taskmanagement.service;

import com.system.taskmanagement.domain.model.Task;
import com.system.taskmanagement.domain.model.TaskStatus;
import com.system.taskmanagement.domain.repositories.TaskRepository;
import com.system.taskmanagement.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task addBook(Task task) {
        task.setStatus(TaskStatus.ONGOING);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task taskRequest) {
        return taskRepository.findById(taskId).map(task -> {
            task.setName(taskRequest.getName());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(TaskStatus.MODIFIED);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Exception"));
    }

    public Task completeTask (Long taskId){
        return taskRepository.findById(taskId).map(task -> {
            task.setStatus(TaskStatus.COMPLETED);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Exception"));
    }
    public Task deleteTask (Long taskId){
        return taskRepository.findById(taskId).map(task -> {
            task.setStatus(TaskStatus.DELETED);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("Exception"));
    }
}

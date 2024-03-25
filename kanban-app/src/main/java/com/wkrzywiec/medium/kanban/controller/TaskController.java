package com.wkrzywiec.medium.kanban.controller;

import com.wkrzywiec.medium.kanban.model.Task;
import com.wkrzywiec.medium.kanban.model.TaskDTO;
import com.wkrzywiec.medium.kanban.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/")
    @ApiOperation(value="View a list of all tasks", response = Task.class, responseContainer = "List")
    public ResponseEntity<?> getAllTasks(){
        try {
            logger.info("Retrieving all tasks");
            return new ResponseEntity<>(
                    taskService.getAllTasks(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all tasks", e);
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Find a task info by its id", response = Task.class)
    public ResponseEntity<?> getTask(@PathVariable Long id){
        try {
            logger.info("Retrieving task with id: {}", id);
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                logger.info("Task with id {} found", id);
                return new ResponseEntity<>(
                        optTask.get(),
                        HttpStatus.OK);
            } else {
                logger.warn("No task found with id: {}", id);
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving task with id: {}", id, e);
            return errorResponse();
        }
    }

    @GetMapping("")
    @ApiOperation(value="Find a task info by its title", response = Task.class)
    public ResponseEntity<?> getTaskByTitle(@RequestParam String title){
        try {
            logger.info("Retrieving task with title: {}", title);
            Optional<Task> optTask = taskService.getTaskByTitle(title);
            if (optTask.isPresent()) {
                logger.info("Task with title {} found", title);
                return new ResponseEntity<>(
                        optTask.get(),
                        HttpStatus.OK);
            } else {
                logger.warn("No task found with title: {}", title);
                return new ResponseEntity<>("No task found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving task with title: {}", title, e);
            return errorResponse();
        }
    }

    @PostMapping("/")
    @ApiOperation(value="Save new task", response = Task.class)
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO){
        try {
            logger.info("Creating new task: {}", taskDTO);
            Task createdTask = taskService.saveNewTask(taskDTO);
            logger.info("New task created: {}", createdTask);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating new task", e);
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value="Update a task with specific id", response = Task.class)
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO){
        try {
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                logger.info("Updating task with id {}: {}", id, taskDTO);
                Task updatedTask = taskService.updateTask(optTask.get(), taskDTO);
                logger.info("Task with id {} updated: {}", id, updatedTask);
                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating task with id " + id, e);
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="Delete Task with specific id", response = String.class)
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        try {
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                logger.info("Deleting task with id {}", id);
                taskService.deleteTask(optTask.get());
                logger.info("Task with id {} deleted", id);
                return new ResponseEntity<>(String.format("Task with id: %d was deleted", id), HttpStatus.OK);
            } else {
                logger.warn("Task with id {} not found for deletion", id);
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting task with id " + id, e);
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        logger.error("Something went wrong");
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<String> noTaskFoundResponse(Long id){
        String errorMessage = "No task found with id: " + id;
        logger.warn(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

}

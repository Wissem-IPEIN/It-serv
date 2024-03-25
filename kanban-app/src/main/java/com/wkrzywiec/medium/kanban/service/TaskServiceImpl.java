package com.wkrzywiec.medium.kanban.service;

import com.wkrzywiec.medium.kanban.model.Task;
import com.wkrzywiec.medium.kanban.model.TaskDTO;
import com.wkrzywiec.medium.kanban.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public List<Task> getAllTasks() {
        logger.info("Retrieving all tasks");
        List<Task> tasksList = new ArrayList<>();
        taskRepository.findAll().forEach(tasksList::add);
        logger.info("All tasks retrieved successfully");
        return tasksList;
    }

    @Override
    @Transactional
    public Optional<Task> getTaskById(Long id) {
        logger.info("Retrieving task with id: {}", id);
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            logger.info("Task with id {} retrieved successfully", id);
        } else {
            logger.warn("Task with id {} not found", id);
        }
        return task;
    }

    @Override
    @Transactional
    public Optional<Task> getTaskByTitle(String title) {
        logger.info("Retrieving task with title: {}", title);
        Optional<Task> task = taskRepository.findByTitle(title);
        if (task.isPresent()) {
            logger.info("Task with title {} retrieved successfully", title);
        } else {
            logger.warn("Task with title {} not found", title);
        }
        return task;
    }

    @Override
    @Transactional
    public Task saveNewTask(TaskDTO taskDTO) {
        logger.info("Saving new task");
        Task task = convertDTOToTask(taskDTO);
        task = taskRepository.save(task);
        logger.info("New task saved successfully with id: {}", task.getId());
        return task;
    }

    @Override
    @Transactional
    public Task updateTask(Task oldTask, TaskDTO newTaskDTO) {
        logger.info("Updating task with id: {}", oldTask.getId());
        Task updatedTask = updateTaskFromDTO(oldTask, newTaskDTO);
        updatedTask = taskRepository.save(updatedTask);
        logger.info("Task with id {} updated successfully", oldTask.getId());
        return updatedTask;
    }

    @Override
    @Transactional
    public void deleteTask(Task task) {
        logger.info("Deleting task with id: {}", task.getId());
        taskRepository.delete(task);
        logger.info("Task with id {} deleted successfully", task.getId());
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }

    private Task updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }

        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }

        if (taskDTO.getColor() != null) {
            task.setColor(taskDTO.getColor());
        }

        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }
        return task;
    }
}

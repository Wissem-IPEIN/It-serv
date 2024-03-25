package com.wkrzywiec.medium.kanban.controller;

import com.wkrzywiec.medium.kanban.model.Kanban;
import com.wkrzywiec.medium.kanban.model.KanbanDTO;
import com.wkrzywiec.medium.kanban.model.Task;
import com.wkrzywiec.medium.kanban.model.TaskDTO;
import com.wkrzywiec.medium.kanban.service.KanbanService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/kanbans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class KanbanController {
    private static final Logger logger = LoggerFactory.getLogger(KanbanController.class);

    private final KanbanService kanbanService;

    @GetMapping("/")
    @ApiOperation(value="View a list of all Kanban boards", response = Kanban.class, responseContainer = "List")
    public ResponseEntity<?> getAllKanbans(){
        logger.info("Received GET request to retrieve all Kanban boards");
        try {
            List<Kanban> kanbanBoards = kanbanService.getAllKanbanBoards();
            logger.info("Retrieved {} Kanban boards", kanbanBoards.size());
            return new ResponseEntity<>(kanbanBoards, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all Kanban boards", e);
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Find a Kanban board info by its id", response = Kanban.class)
    public ResponseEntity<?> getKanban(@PathVariable Long id){
        logger.info("Received GET request to retrieve Kanban board with id {}", id);
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                logger.info("Found Kanban board with id {}: {}", id, optKanban.get());
                return new ResponseEntity<>(optKanban.get(), HttpStatus.OK);
            } else {
                logger.warn("Kanban board with id {} not found", id);
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching Kanban board with id {}", id, e);
            return errorResponse();
        }
    }

    @GetMapping("")
    @ApiOperation(value="Find a Kanban board info by its title", response = Kanban.class)
    public ResponseEntity<?> getKanbanByTitle(@RequestParam String title){
        logger.info("Received GET request to retrieve Kanban board with title '{}'", title);
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanByTitle(title);
            if (optKanban.isPresent()) {
                logger.info("Found Kanban board with title '{}': {}", title, optKanban.get());
                return new ResponseEntity<>(optKanban.get(), HttpStatus.OK);
            } else {
                logger.warn("Kanban board with title '{}' not found", title);
                return new ResponseEntity<>("No kanban found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching Kanban board with title '{}'", title, e);
            return errorResponse();
        }
    }

    @PostMapping("/")
    @ApiOperation(value="Save new Kanban board", response = Kanban.class)
    public ResponseEntity<?> createKanban(@RequestBody KanbanDTO kanbanDTO){
        logger.info("Received POST request to create a new Kanban board: {}", kanbanDTO);
        try {
            Kanban createdKanban = kanbanService.saveNewKanban(kanbanDTO);
            logger.info("New Kanban board created successfully: {}", createdKanban);
            return new ResponseEntity<>(createdKanban, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating a new Kanban board: {}", kanbanDTO, e);
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value="Update a Kanban board with specific id", response = Kanban.class)
    public ResponseEntity<?> updateKanban(@PathVariable Long id, @RequestBody KanbanDTO kanbanDTO){
        logger.info("Received PUT request to update Kanban board with id: {}, new data: {}", id, kanbanDTO);
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                Kanban updatedKanban = kanbanService.updateKanban(optKanban.get(), kanbanDTO);
                logger.info("Kanban board with id {} updated successfully: {}", id, updatedKanban);
                return new ResponseEntity<>(updatedKanban, HttpStatus.OK);
            } else {
                logger.warn("No Kanban board found with id: {}", id);
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating Kanban board with id: {} and data: {}", id, kanbanDTO, e);
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="Delete Kanban board with specific id", response = String.class)
    public ResponseEntity<?> deleteKanban(@PathVariable Long id){
        logger.info("Received DELETE request to delete Kanban board with id: {}", id);
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(id);
            if (optKanban.isPresent()) {
                kanbanService.deleteKanban(optKanban.get());
                logger.info("Kanban board with id: {} was deleted successfully", id);
                return new ResponseEntity<>(String.format("Kanban with id: %d was deleted", id), HttpStatus.OK);
            } else {
                logger.warn("No Kanban board found with id: {}", id);
                return noKanbanFoundResponse(id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting Kanban board with id: {}", id, e);
            return errorResponse();
        }
    }

    @GetMapping("/{kanbanId}/tasks/")
    @ApiOperation(value="View a list of all tasks for a Kanban with provided id", response = Task.class, responseContainer = "List")
    public ResponseEntity<?> getAllTasksInKanban(@PathVariable Long kanbanId){
        logger.info("Received GET request to retrieve all tasks for Kanban with id: {}", kanbanId);
        try {
            Optional<Kanban> optKanban = kanbanService.getKanbanById(kanbanId);
            if (optKanban.isPresent()) {
                logger.info("Found Kanban board with id: {}. Returning all tasks.", kanbanId);
                return new ResponseEntity<>(optKanban.get().getTasks(), HttpStatus.OK);
            } else {
                logger.warn("No Kanban board found with id: {}. Unable to retrieve tasks.", kanbanId);
                return noKanbanFoundResponse(kanbanId);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving tasks for Kanban with id: {}", kanbanId, e);
            return errorResponse();
        }
    }

    @PostMapping("/{kanbanId}/tasks/")
    @ApiOperation(value="Save new Task and assign it to Kanban board", response = Kanban.class)
    public ResponseEntity<?> createTaskAssignedToKanban(@PathVariable Long kanbanId, @RequestBody TaskDTO taskDTO){
        logger.info("Received POST request to create a new task for Kanban with id: {}", kanbanId);
        try {
            Kanban createdKanban = kanbanService.addNewTaskToKanban(kanbanId, taskDTO);
            logger.info("New task created and assigned to Kanban with id: {}", kanbanId);
            return new ResponseEntity<>(createdKanban, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating and assigning task to Kanban with id: {}", kanbanId, e);
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        logger.error("Internal server error occurred.");
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noKanbanFoundResponse(Long id){
        logger.warn("No kanban found with id: {}", id);
        return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

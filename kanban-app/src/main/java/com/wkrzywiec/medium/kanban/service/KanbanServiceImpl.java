package com.wkrzywiec.medium.kanban.service;

import com.wkrzywiec.medium.kanban.model.Kanban;
import com.wkrzywiec.medium.kanban.model.KanbanDTO;
import com.wkrzywiec.medium.kanban.model.Task;
import com.wkrzywiec.medium.kanban.model.TaskDTO;
import com.wkrzywiec.medium.kanban.repository.KanbanRepository;
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
public class KanbanServiceImpl implements KanbanService {

    private static final Logger logger = LoggerFactory.getLogger(KanbanServiceImpl.class);

    private final KanbanRepository kanbanRepository;

    @Override
    @Transactional
    public List<Kanban> getAllKanbanBoards() {
        logger.info("Retrieving all Kanban boards");
        List<Kanban> kanbanList = new ArrayList<>();
        kanbanRepository.findAll().forEach(kanbanList::add);
        logger.info("All Kanban boards retrieved successfully");
        return kanbanList;
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanById(Long id) {
        logger.info("Retrieving Kanban board with id: {}", id);
        Optional<Kanban> kanban = kanbanRepository.findById(id);
        if (kanban.isPresent()) {
            logger.info("Kanban board with id {} retrieved successfully", id);
        } else {
            logger.warn("Kanban board with id {} not found", id);
        }
        return kanban;
    }

    @Override
    @Transactional
    public Optional<Kanban> getKanbanByTitle(String title) {
        logger.info("Retrieving Kanban board with title: {}", title);
        Optional<Kanban> kanban = kanbanRepository.findByTitle(title);
        if (kanban.isPresent()) {
            logger.info("Kanban board with title {} retrieved successfully", title);
        } else {
            logger.warn("Kanban board with title {} not found", title);
        }
        return kanban;
    }

    @Override
    @Transactional
    public Kanban saveNewKanban(KanbanDTO kanbanDTO) {
        logger.info("Saving new Kanban board");
        Kanban kanban = convertDTOToKanban(kanbanDTO);
        kanban = kanbanRepository.save(kanban);
        logger.info("New Kanban board saved successfully with id: {}", kanban.getId());
        return kanban;
    }

    @Override
    @Transactional
    public Kanban updateKanban(Kanban oldKanban, KanbanDTO newKanbanDTO) {
        logger.info("Updating Kanban board with id: {}", oldKanban.getId());
        oldKanban.setTitle(newKanbanDTO.getTitle());
        oldKanban = kanbanRepository.save(oldKanban);
        logger.info("Kanban board with id {} updated successfully", oldKanban.getId());
        return oldKanban;
    }

    @Override
    @Transactional
    public void deleteKanban(Kanban kanban) {
        logger.info("Deleting Kanban board with id: {}", kanban.getId());
        kanbanRepository.delete(kanban);
        logger.info("Kanban board with id {} deleted successfully", kanban.getId());
    }

    @Override
    @Transactional
    public Kanban addNewTaskToKanban(Long kanbanId, TaskDTO taskDTO) {
        logger.info("Adding new task to Kanban board with id: {}", kanbanId);
        Kanban kanban = kanbanRepository.findById(kanbanId).get();
        kanban.addTask(convertDTOToTask(taskDTO));
        kanban = kanbanRepository.save(kanban);
        logger.info("New task added to Kanban board with id {}", kanbanId);
        return kanban;
    }

    private Kanban convertDTOToKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setTitle(kanbanDTO.getTitle());
        return kanban;
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }
}

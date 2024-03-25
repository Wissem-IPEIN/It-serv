package com.wkrzywiec.medium.kanban;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.micrometer.core.annotation.Timed;

@SpringBootApplication
@EntityScan("com.wkrzywiec.medium.kanban.model")
@EnableJpaRepositories("com.wkrzywiec.medium.kanban.repository")
public class KanbanApplication {

	private static final Logger logger = LoggerFactory.getLogger(KanbanApplication.class);

	@Timed(value = "kanban_application_startup_duration", description = "Duration of Kanban application startup")
	public static void main(String[] args) {
		logger.info("Starting Kanban application...");
		long startTime = System.currentTimeMillis();
		SpringApplication.run(KanbanApplication.class, args);
		long endTime = System.currentTimeMillis();
		logger.info("Kanban application started in {} milliseconds.", (endTime - startTime));
	}

}

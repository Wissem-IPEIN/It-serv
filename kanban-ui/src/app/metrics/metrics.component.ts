import { Component, OnInit } from '@angular/core';
import { TaskService } from '../service/task.service';
import { KanbanService } from '../service/kanban-service.service';

@Component({
  selector: 'app-metrics',
  templateUrl: './metrics.component.html',
  styleUrls: ['./metrics.component.css']
})
export class MetricsComponent implements OnInit {
  taskMetricsData: string;
  kanbanMetricsData: string;

  constructor(private taskService: TaskService, private kanbanService: KanbanService) {}

  ngOnInit(): void {
    this.fetchTaskMetrics();
    this.fetchKanbanMetrics();
  }

  fetchTaskMetrics(): void {
    this.taskService.getPrometheusMetrics().subscribe(
      (data) => {
        this.taskMetricsData = data;
      },
      (error) => {
        console.error('Error fetching task metrics:', error);
      }
    );
  }

  fetchKanbanMetrics(): void {
    this.kanbanService.getPrometheusMetrics().subscribe(
      (data) => {
        this.kanbanMetricsData = data;
      },
      (error) => {
        console.error('Error fetching kanban metrics:', error);
      }
    );
  }
}

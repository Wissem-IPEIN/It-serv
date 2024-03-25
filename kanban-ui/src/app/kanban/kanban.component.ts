import { Component, OnInit } from '@angular/core';
import { KanbanService } from '../service/kanban-service.service';
import { ActivatedRoute } from '@angular/router';
import { Kanban } from '../model/kanban/kanban';
import { Task } from '../model/task/task';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-kanban',
  templateUrl: './kanban.component.html',
  styleUrls: ['./kanban.component.css']
})
export class KanbanComponent implements OnInit {

  kanban: Kanban;
  todos: Task[] = [];
  inprogress: Task[] = [];
  dones: Task[] = [];

  constructor(
    private kanbanService: KanbanService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    console.log('KanbanComponent initialized');
    this.getKanban();
  }

  drop(event: CdkDragDrop<string[]>) {
    console.log('Drop event:', event);
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      console.log('Updating task status after drag and drop');
      this.updateTaskStatusAfterDragDrop(event);
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  openDialogForNewTask(): void {
    console.log('Opening dialog for creating a new task');
    this.openDialog('Create New Task', new Task());
  }

  openTaskDialog(event): void {
    console.log('Opening task dialog:', event);
    let taskId = event.srcElement.id;

    this.taskService.getTaskById(taskId).subscribe(
      response => {
        console.log('Task retrieved successfully:', response);
        this.openDialog('Update Task', response);
      },
      error => {
        console.error('Error retrieving task:', error);
      }
    );
  }

  private getKanban(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log('Retrieving Kanban with ID:', id);

    this.kanbanService.retrieveKanbanById(id).subscribe(
      response => {
        console.log('Kanban retrieved successfully:', response);
        this.kanban = response;
        this.splitTasksByStatus(response);
      },
      error => {
        console.error('Error retrieving Kanban:', error);
      }
    );
  }

  private splitTasksByStatus(kanban: Kanban): void {
    console.log('Splitting tasks by status');
    this.todos = kanban.tasks.filter(t=>t.status==='TODO');
    this.inprogress = kanban.tasks.filter(t=>t.status==='INPROGRESS');
    this.dones = kanban.tasks.filter(t=>t.status==='DONE');
  }

  private updateTaskStatusAfterDragDrop(event: CdkDragDrop<string[], string[]>) {
    console.log('Updating task status after drag and drop:', event);
    let taskId = event.item.element.nativeElement.id;
    let containerId = event.container.id;

    this.taskService.getTaskById(taskId).subscribe(
      response => {
        console.log('Task retrieved for update:', response);
        this.updateTaskStatus(response, containerId);
      },
      error => {
        console.error('Error retrieving task for update:', error);
      }
    );
  }

  private updateTaskStatus(task: Task, containerId: string): void {
    console.log('Updating task status:', task, containerId);
    if (containerId === 'todo'){
      task.status = 'TODO'
    } else if (containerId === 'inpro'){
      task.status = 'INPROGRESS'
    } else {
      task.status = 'DONE'
    }
    this.taskService.updateTask(task).subscribe(
      () => {
        console.log('Task status updated successfully');
      },
      error => {
        console.error('Error updating task status:', error);
      }
    );
  }

  private openDialog(title: string, task: Task): void {
    console.log('Opening task dialog with title:', title);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      title: title,
      task: task,
      kanbanId: this.kanban.id
    };
    this.dialog.open(TaskDialogComponent, dialogConfig)
  }
}

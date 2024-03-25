import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material';
import { Task } from '../model/task/task';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { KanbanService } from '../service/kanban-service.service';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-task-dialog',
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.css']
})
export class TaskDialogComponent implements OnInit {

  dialogTitle: String;
  kanbanId: String;
  task: Task;

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private kanbanService: KanbanService,
    private taskService: TaskService) {

    this.dialogTitle = data.title;
    this.kanbanId = data.kanbanId;
    this.task = data.task;

    this.form = fb.group({
      title: [this.task.title, Validators.required],
      description: [this.task.description, Validators.required],
      color: [this.task.color, Validators.required]
    });

    console.log('TaskDialogComponent initialized');
  }

  ngOnInit() {
    console.log('TaskDialogComponent ngOnInit called');
  }

  save() {
    console.log('Save button clicked');

    this.mapFormToTaskModel();
    if (!this.task.id) {
      console.log('Saving new task in kanban');
      this.kanbanService.saveNewTaskInKanban(this.kanbanId, this.task).subscribe(() => {
        console.log('New task saved successfully');
      }, error => {
        console.error('Error saving new task:', error);
      });
    } else {
      console.log('Updating task');
      this.taskService.updateTask(this.task).subscribe(() => {
        console.log('Task updated successfully');
      }, error => {
        console.error('Error updating task:', error);
      });
    }

    this.dialogRef.close();
    window.location.reload();
  }

  close() {
    console.log('Close button clicked');
    this.dialogRef.close();
  }

  private mapFormToTaskModel(): void {
    this.task.title = this.form.get('title').value;
    this.task.description = this.form.get('description').value;
    this.task.color = this.form.get('color').value;
    this.task.status = 'TODO';
  }

}

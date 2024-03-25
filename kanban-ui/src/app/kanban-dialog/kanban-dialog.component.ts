import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { KanbanService } from '../service/kanban-service.service';
import { Kanban } from '../model/kanban/kanban';

@Component({
  selector: 'app-kanban-dialog',
  templateUrl: './kanban-dialog.component.html',
  styleUrls: ['./kanban-dialog.component.css']
})
export class KanbanDialogComponent implements OnInit {

  title: string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<KanbanDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private kanbanService: KanbanService
  ) {
    console.log('Kanban dialog component initialized with data:', data);

    this.form = fb.group({
      title: [this.title, Validators.required]
    });

    console.log('Form initialized:', this.form);
  }

  ngOnInit() {
    console.log('Kanban dialog component initialized');
  }

  close() {
    console.log('Closing dialog');
    this.dialogRef.close();
  }

  save() {
    console.log('Saving new Kanban board');
    this.title = this.form.get('title').value;
    console.log('Title extracted from form:', this.title);

    if (this.title) {
      this.kanbanService.saveNewKanban(this.title).subscribe(
        response => {
          console.log('Kanban board saved successfully:', response);
        },
        error => {
          console.error('Error saving Kanban board:', error);
        }
      );
    }

    this.dialogRef.close();
    window.location.reload();
  }
}

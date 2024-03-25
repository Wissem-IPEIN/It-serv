import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../model/task/task';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private kanbanAppUrl = environment.kanbanAppUrl;

  constructor(private http: HttpClient) { }

  updateTask(task: Task): Observable<Task> {
    console.log('Updating task:', task);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    return this.http.put<Task>(
      this.kanbanAppUrl + '/tasks/' + task.id,
      task,
      options
    );
  }

  getTaskById(id: string): Observable<Task> {
    console.log('Retrieving task by ID:', id);
    return this.http.get<Task>(this.kanbanAppUrl + '/tasks/' + id);
  }
}

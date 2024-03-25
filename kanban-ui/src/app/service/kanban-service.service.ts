import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Kanban } from '../model/kanban/kanban';
import { Task } from '../model/task/task';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class KanbanService {

  private kanbanAppUrl = environment.kanbanAppUrl;

  constructor(private http: HttpClient) { }

  retrieveAllKanbanBoards(): Observable<Kanban[]> {
    console.log('Retrieving all Kanban boards');
    return this.http.get<Kanban[]>(this.kanbanAppUrl + '/kanbans/');
  }

  retrieveKanbanById(id: String): Observable<Kanban> {
    console.log('Retrieving Kanban by ID:', id);
    return this.http.get<Kanban>(this.kanbanAppUrl + '/kanbans/' + id);
  }

  saveNewKanban(title: string): Observable<string> {
    console.log('Saving new Kanban with title:', title);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    const jsonObject = this.prepareTitleJsonObject(title);
    return this.http.post<string>(
      this.kanbanAppUrl + '/kanbans/',
      jsonObject,
      options
    );
  }

  saveNewTaskInKanban(kanbanId: String, task: Task): Observable<Task> {
    console.log('Saving new task in Kanban with ID:', kanbanId);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    return this.http.post<Task>(
      this.kanbanAppUrl + '/kanbans/' + kanbanId + '/tasks/',
      task,
      options
    );
  }

  private prepareTitleJsonObject(title: string) {
    const object = {
      title: title
    };
    return JSON.stringify(object);
  }

}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Kanban } from '../model/kanban/kanban';
import { Task } from '../model/task/task';
import { environment } from 'src/environments/environment';
import { Counter } from 'prom-client';

@Injectable({
  providedIn: 'root'
})
export class KanbanService {

  private kanbanAppUrl = environment.kanbanAppUrl;
  private httpRequestCounter: Counter;


  constructor(private http: HttpClient) {
    // Initialize the Counter in the constructor
    this.httpRequestCounter = new Counter({
      name: 'http_requests_total',
      help: 'Total number of HTTP requests made',
      labelNames: ['method', 'endpoint']
    });
  }


  retrieveAllKanbanBoards(): Observable<Kanban[]> {
    this.httpRequestCounter.inc({ method: 'GET', endpoint: '/kanbans/' });
    return this.http.get<Kanban[]>(`${this.kanbanAppUrl}/kanbans/`).pipe(
      catchError(this.handleError)
    );
  }

  retrieveKanbanById(id: string): Observable<Kanban> {
    this.httpRequestCounter.inc({ method: 'GET', endpoint: `/kanbans/${id}` });
    return this.http.get<Kanban>(`${this.kanbanAppUrl}/kanbans/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  saveNewKanban(title: string): Observable<string> {
    this.httpRequestCounter.inc({ method: 'POST', endpoint: '/kanbans/' });
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    const jsonObject = this.prepareTitleJsonObject(title);
    return this.http.post<string>(
      `${this.kanbanAppUrl}/kanbans/`,
      jsonObject,
      options
    ).pipe(
      catchError(this.handleError)
    );
  }

  saveNewTaskInKanban(kanbanId: string, task: Task): Observable<Task> {
    this.httpRequestCounter.inc({ method: 'POST', endpoint: `/kanbans/${kanbanId}/tasks/` });
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    return this.http.post<Task>(
      `${this.kanbanAppUrl}/kanbans/${kanbanId}/tasks/`,
      task,
      options
    ).pipe(
      catchError(this.handleError)
    );
  }

  getPrometheusMetrics(): Observable<string> {
    // In a real application, this would fetch metrics data from the server
    // For demonstration, we return a mock string
    const metricsData = `
    # HELP http_requests_total Total number of HTTP requests made
    # TYPE http_requests_total counter
    http_requests_total{method="GET", endpoint="/tasks/"} 100
    http_requests_total{method="PUT", endpoint="/tasks/"} 50
  `;
    // Return the metrics data as an observable
    return new Observable<string>((observer) => {
      observer.next(metricsData);
      observer.complete();
    });
  }

  private prepareTitleJsonObject(title: string) {
    return JSON.stringify({ title: title });
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(error);
  }
}

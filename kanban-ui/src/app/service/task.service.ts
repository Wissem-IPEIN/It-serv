import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Task } from '../model/task/task';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Counter } from 'prom-client';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

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



  updateTask(task: Task): Observable<Task> {
    this.httpRequestCounter.inc({ method: 'PUT', endpoint: `/tasks/${task.id}` });
    console.log('Updating task:', task);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    return this.http.put<Task>(
      `${this.kanbanAppUrl}/tasks/${task.id}`,
      task,
      options
    ).pipe(
      catchError(this.handleError)
    );
  }

  getTaskById(id: string): Observable<Task> {
    this.httpRequestCounter.inc({ method: 'GET', endpoint: `/tasks/${id}` });
    console.log('Retrieving task by ID:', id);
    return this.http.get<Task>(`${this.kanbanAppUrl}/tasks/${id}`).pipe(
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


  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(error);
  }
}

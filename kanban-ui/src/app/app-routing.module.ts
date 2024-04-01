import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { KanbanComponent } from './kanban/kanban.component';
import { MetricsComponent } from './metrics/metrics.component'; // Import MetricsComponent

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'kanbans/:id', component: KanbanComponent },
  { path: 'metrics', component: MetricsComponent } // Add route for MetricsComponent
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  constructor() {
    console.log('AppRoutingModule initialized');
  }
}

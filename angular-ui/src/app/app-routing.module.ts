import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AddBillComponent } from './add-bill/add-bill.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'add-bill', component: AddBillComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

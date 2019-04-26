import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AddBillComponent } from './add-bill/add-bill.component';
import { BillsListComponent } from './bills/bills-list.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'add-bill', component: AddBillComponent},
  {path: 'bills', component: BillsListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

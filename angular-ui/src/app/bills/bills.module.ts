import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BillsListComponent } from './bills-list.component';
import { TableModule } from 'primeng/table';

@NgModule({
  declarations: [BillsListComponent],
  imports: [
    CommonModule,
    TableModule
  ]
})
export class BillsModule { }

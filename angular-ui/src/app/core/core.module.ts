import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoreService } from './service/store.service';
import { ProductsService } from './service/products.service';
import { BillService } from './service/bill.service';

@NgModule({
  declarations: [],
  providers: [
    StoreService,
    BillService,
    ProductsService
  ],
  imports: [
    CommonModule
  ]
})
export class CoreModule { }

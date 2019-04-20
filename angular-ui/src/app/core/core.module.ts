import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StoreService } from './service/store.service';
import { ProductsService } from './service/products.service';

@NgModule({
  declarations: [],
  providers: [
    StoreService,
    ProductsService
  ],
  imports: [
    CommonModule
  ]
})
export class CoreModule { }

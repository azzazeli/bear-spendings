import {Component, OnInit} from '@angular/core';
import {Bill} from '../core/model/bill.model';
import {BillService} from '../core/service/bill.service';
import {NGXLogger} from 'ngx-logger';
import {StoreService} from '../core/service/store.service';
import {Store} from '../core/model/store.model';
import {Observable} from 'rxjs';
import {Product} from '../core/model/product.model';
import {ProductsService} from '../core/service/products.service';
import {environment} from '../../environments/environment';
import * as moment from 'moment';
import {LazyLoadEvent} from "primeng/api";

@Component({
  selector: 'app-bills-list',
  templateUrl: './bills-list.component.html',
  styleUrls: ['./bills-list.component.css']
})
export class BillsListComponent implements OnInit {
  bills: Bill[];
  totalRecords: number = 0;
  loading: boolean = false;
  readonly PAGE_SIZE = 10;

  constructor(private logger: NGXLogger,
              private storeService: StoreService,
              private productService: ProductsService,
              private billService: BillService) {
  }

  ngOnInit() {
    this.logger.debug(`Initializing BillsListComponent ...`);
    this.billService.allBillsCount().subscribe(value => {
      this.totalRecords = value;
    });
  }

  loadBills(event: LazyLoadEvent) {
    // use setTimeout to avoid ExpressionChangedAfterItHasBeenCheckedError
    setTimeout(() => {
      this.logger.debug(`Loading bills first:${event.first} rows:${event.rows}`);
      this.loading = true;
      const page = event.first / 10;
      this.billService.allBills(page, event.rows).subscribe((bills: Bill[]) => {
        this.bills = bills;
        this.loading = false;
      });
    });
  }

  getStore(storeId: number): Observable<Store> {
    return this.storeService.getObservableById(storeId);
  }

  getProduct(productId: number): Observable<Product> {
    return this.productService.getObservableById(productId);
  }

  //TODO: maybe part of common utils service
  formatMoment(date: string): string {
    return moment(date).format(environment.dateFormat);
  }


}

import { Component, OnInit } from '@angular/core';
import { Bill } from '../core/model/bill.model';
import { BillService } from '../core/service/bill.service';
import { NGXLogger } from 'ngx-logger';
import { StoreService } from '../core/service/store.service';
import { Store } from '../core/model/store.model';

@Component({
  selector: 'app-bills-list',
  templateUrl: './bills-list.component.html',
  styleUrls: ['./bills-list.component.css']
})
export class BillsListComponent implements OnInit {
  bills: Bill[];

  constructor(private logger: NGXLogger,
              private storeService: StoreService,
              private billService: BillService) {
  }

  ngOnInit() {
    this.billService.allBills().subscribe((bills: Bill[]) => {
      bills.forEach( (bill:Bill) => {
        this.storeService.getStore(bill.storeId).subscribe((store: Store) => {
            bill.store = store;
          }
        );
      });
      this.bills = bills;
    });
  }

}

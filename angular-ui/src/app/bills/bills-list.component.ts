import { Component, OnInit } from '@angular/core';
import { Bill } from '../core/model/bill.model';
import { BillService } from '../core/service/bill.service';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-bills-list',
  templateUrl: './bills-list.component.html',
  styleUrls: ['./bills-list.component.css']
})
export class BillsListComponent implements OnInit {
  bills: Bill[];

  constructor(private logger: NGXLogger, private billService: BillService) {
  }

  ngOnInit() {
    this.billService.allBills().subscribe((bills: Bill[]) => {
      this.bills = bills;
    })
  }

}

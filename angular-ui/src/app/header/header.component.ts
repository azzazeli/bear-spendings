import { Component, OnInit } from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {BillService} from '../core/service/bill.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private logger: NGXLogger, private billService: BillService) { }

  ngOnInit() {
  }

  exportAll() {
    this.logger.debug('Exporting all bills ...');
    this.billService.exportAll();
  }

}

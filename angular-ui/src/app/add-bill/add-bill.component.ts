import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { StoreService } from '../store.service';
import { Store } from '../model/store.model';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css']
})
export class AddBillComponent implements OnInit {

  stores: string[] = ['Nr.1', 'Ali Market', 'Pegas', 'Ali Market'];
  addBillForm: FormGroup;

  constructor(private logger: NGXLogger, private storeService: StoreService) { }

  ngOnInit() {
    this.storeService.getStores().subscribe((stores: Store[])  => {
      this.logger.debug('subscribe to getStores(): ', stores);
    });

    this.addBillForm = new FormGroup({
      store: new FormControl(null)
    });
  }

  onAddBill() {
    this.logger.debug("on add bill. addBillForm:", this.addBillForm );
  }

}

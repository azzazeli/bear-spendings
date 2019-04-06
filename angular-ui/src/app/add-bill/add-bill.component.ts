import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { StoreService } from '../store.service';
import { Store } from '../model/store.model';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css']
})
export class AddBillComponent implements OnInit {

  stores: Store[];
  addBillForm: FormGroup;

  constructor(private logger: NGXLogger, private storeService: StoreService) { }

  ngOnInit() {
    this.storeService.getStores().subscribe((stores: Store[])  => {
      this.logger.debug('subscribe to getStores(): ', stores);
      this.stores = stores;
    });

    this.addBillForm = new FormGroup({
      store: new FormControl(null, Validators.required)
    });
  }

  onStoreSelected() {
    this.logger.debug('on store selected');
  }

  onAddBill() {
    this.logger.debug("on add bill. addBillForm:", this.addBillForm );
    // this.addBillForm.vali
  }

}

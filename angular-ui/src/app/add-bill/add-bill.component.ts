import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { StoreService } from '../service/store.service';
import { Store } from '../model/store.model';
import { Product } from '../model/product.model';
import { of, Observable } from 'rxjs';
// import { of } from 'rxjs/internal/observable/of';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css']
})
export class AddBillComponent implements OnInit {

  stores: Store[];
  addBillForm: FormGroup;
  topStoreProducts: Observable<Product[]>;

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
    const storeId: number = this.addBillForm.get('store').value;
    this.logger.debug('on store selected. store id:' + storeId);
    this.topStoreProducts = of([new Product(1, 'Chefir JLC 2.5%', 9.80),
      new Product(1, 'Chefir JLC 1.5%', 7.85)]);
  }

  onAddBill() {
    this.logger.debug("on add bill. addBillForm:", this.addBillForm );
    // this.addBillForm.vali
  }

}

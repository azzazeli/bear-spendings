import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {AbstractControl, FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {NGXLogger} from 'ngx-logger';
import {StoreService} from '../core/service/store.service';
import {Store} from '../core/model/store.model';
import {Product} from '../core/model/product.model';
import {BillItem} from '../core/model/bill-item.model';
import {NewBillItemComponent} from './new-bill-item/new-bill-item.component';
import {ProductsService} from '../core/service/products.service';
import {BillService} from '../core/service/bill.service';
import {Bill} from '../core/model/bill.model';
import * as moment from 'moment';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AddBillComponent implements OnInit {
  stores: Store[];
  addBillForm: FormGroup;
  topStoreProducts: Product[];
  selectedProductId: number;

  @ViewChild(NewBillItemComponent)
  newBillItemComponent: NewBillItemComponent;

  constructor(private logger: NGXLogger, private storeService: StoreService,
              private billService: BillService,
              private productsService: ProductsService) {
  }

  ngOnInit() {
    this.storeService.getStores().subscribe((stores: Store[])  => {
      this.logger.debug('AddBillComponent: Subscribe to getStores(): ', stores);
      this.stores = stores;
    });
    this.addBillForm = new FormGroup({
      'store-id': new FormControl(null, Validators.required),
      'bill-date': new FormControl(null, Validators.required),
      'bill-items': new FormArray([], Validators.required)
    });
  }

  get billItemsControls(): AbstractControl[] {
    return this.billItems().controls;
  }

  onStoreSelected() {
    const storeId: number = this.addBillForm.get('store-id').value;
    this.logger.debug('AddBillComponent: On store selected. store id:', storeId);
    this.productsService.topStoreProducts(storeId).subscribe((products) => {
      this.topStoreProducts = products;
    });
    this.resetNewBillItem();
  }

  onTopProductSelected(productId: number) {
    this.selectedProductId = productId;
    this.logger.debug('AddBillComponent: On top store product selected. ProductId:', productId );
    const selectedProduct: Product = this.topStoreProducts.find(p => p.id == productId);
    this.newBillItemComponent.setBillItem(new BillItem(selectedProduct.id, selectedProduct.name, 1, selectedProduct.price));
  }

  onAddBillItem(billItem: BillItem) {
    this.logger.debug('AddBillComponent: On add bill item: ', JSON.stringify(billItem));
    this.billItems().push(new FormGroup(
      {
        'product-id': new FormControl(billItem.productId),
        'product-name': new FormControl(billItem.productName),
        'quantity': new FormControl(billItem.quantity),
        'price': new FormControl(billItem.price)
      }
    ));
    this.resetNewBillItem();
  }

  onDeleteBillItem(index: number) {
    this.logger.debug('AddBillComponent: On delete bill item at index:', index);
    this.billItems().removeAt(index);
  }

  onAddBill() {
    this.logger.debug("AddBillComponent: On add bill. addBillForm:", this.addBillForm );
    const bill: Bill = new Bill(moment(this.addBillForm.get('bill-date').value), this.addBillForm.get('store-id').value);
    for( let billItemFG of this.billItems().controls) {
      bill.items.push(new BillItem(
        billItemFG.get('product-id').value,
        billItemFG.get('product-name').value,
        billItemFG.get('quantity').value,
        billItemFG.get('price').value)
      );
    }
    this.billService.addBill(bill).subscribe(addedBill => {
      this.logger.debug("Bill was added. bill id:" + addedBill.id);
      this.resetForm();
    });
  }

  private billItems(): FormArray {
    return <FormArray>this.addBillForm.get('bill-items');
  }

  private resetForm() {
    this.logger.debug('resetting new bill form ...');
    this.addBillForm.get('bill-date').setValue(null);
    this.addBillForm.get('store-id').setValue(null);
    this.topStoreProducts.splice(0);
    this.billItems().controls = [];
    this.resetNewBillItem();
    this.addBillForm.markAsUntouched();
    this.addBillForm.markAsPristine();
    this.logger.debug("resetting done.")
  }

  private resetNewBillItem() {
    this.logger.debug('resetting new bill item form ...');
    this.newBillItemComponent.reset();
    this.selectedProductId = null;
    this.logger.debug("resetting done.");
  }

}

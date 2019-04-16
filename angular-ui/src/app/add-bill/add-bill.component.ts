import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { StoreService } from '../service/store.service';
import { Store } from '../model/store.model';
import { Product } from '../model/product.model';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css']
})
export class AddBillComponent implements OnInit {

  stores: Store[];
  addBillForm: FormGroup;
  topStoreProducts: Product[];
  selectedProductId: number;

  constructor(private logger: NGXLogger, private storeService: StoreService) { }

  ngOnInit() {
    this.storeService.getStores().subscribe((stores: Store[])  => {
      this.logger.debug('Subscribe to getStores(): ', stores);
      this.stores = stores;
    });
    this.addBillForm = new FormGroup({
      'store': new FormControl(null, Validators.required),
      'new-bill-item': new FormGroup({
        'product-id': new FormControl(null),
        'product-name': new FormControl(null, Validators.required),
        'price': new FormControl(null, Validators.required),
        'quantity': new FormControl(null, Validators.required)
      }),
      'bill-items': new FormArray([])
    });
  }

  get billItemsControls(): AbstractControl[] {
    return this.billItems().controls;
  }

  onStoreSelected() {
    const storeId: number = this.addBillForm.get('store').value;
    this.logger.debug('On store selected. store id:', storeId);
    this.topStoreProducts = [new Product(1, 'Chefir JLC 2.5%', 9.80),
      new Product(2, 'Chefir JLC 1.5%', 7.85)];
    this.resetNewBillItem();
  }

  onTopProductSelected(productId: number) {
    this.selectedProductId = productId;
    this.logger.debug('On top store product selected. ProductId:', productId );
    const selectedProduct: Product = this.topStoreProducts.find(p => p.id == productId);
    this.addBillForm.get('new-bill-item').setValue({
      'product-id': selectedProduct.id,
      'product-name': selectedProduct.name,
      'quantity': 1,
      'price': selectedProduct.price
    });
  }

  onAddBillItem() {
    this.logger.debug('On add bill item: ', JSON.stringify(this.addBillForm.get('new-bill-item').value));
    this.billItems().push(new FormGroup(
      {
        'product-id': new FormControl(this.addBillForm.get('new-bill-item.product-id').value),
        'product-name': new FormControl(this.addBillForm.get('new-bill-item.product-name').value),
        'quantity': new FormControl(this.addBillForm.get('new-bill-item.quantity').value),
        'price': new FormControl(this.addBillForm.get('new-bill-item.price').value)
      }
    ));
    this.resetNewBillItem();
  }

  onDeleteBillItem(index: number) {
    this.logger.debug('On delete bill item at index:', index);
    this.billItems().removeAt(index);
  }

  onAddBill() {
    this.logger.debug("On add bill. addBillForm:", this.addBillForm );
    // this.addBillForm.vali
  }

  private billItems(): FormArray {
    return <FormArray>this.addBillForm.get('bill-items');
  }

  private resetNewBillItem() {
    this.addBillForm.get('new-bill-item').reset();
    this.selectedProductId = null;
  }

}

import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BillItem} from '../../core/model/bill-item.model';
import {NGXLogger} from 'ngx-logger';

@Component({
  selector: 'app-new-bill-item',
  templateUrl: './new-bill-item.component.html',
  styleUrls: ['./new-bill-item.component.css']
})
export class NewBillItemComponent implements OnInit {
  newBillItemForm: FormGroup;

  @Output('add-bill-item')
  addBillItemEvent: EventEmitter<BillItem> = new EventEmitter();
  billItem: BillItem;
  pricePerUnit : number;

  constructor(private logger: NGXLogger) { }

  ngOnInit() {
    this.newBillItemForm = new FormGroup({
      'product-id': new FormControl(null),
      'product-name': new FormControl(null, Validators.required),
      'price': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative]),
      'quantity': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative])
    });
  }

  setBillItem(billItem: BillItem): void {
    this.logger.debug('NewBillItemComponent: Set bill item:' + JSON.stringify(billItem));
    this.billItem = billItem;
    this.newBillItemForm.get('product-id').setValue(billItem.productId);
    this.newBillItemForm.get('product-name').setValue(billItem.productName);
    this.newBillItemForm.get('price').setValue(billItem.price);
    this.newBillItemForm.get('quantity').setValue(billItem.quantity);
    this.calculatePricePerUnit(billItem.price, billItem.quantity);
  }

  reset(): void {
    this.newBillItemForm.reset();
  }

  onAddBillItem(): void {
    this.logger.debug('NewBillItemComponent: On add bill item');
    this.billItem = new BillItem(
      this.newBillItemForm.get('product-id').value,
      this.newBillItemForm.get('product-name').value,
      this.newBillItemForm.get('quantity').value,
      this.newBillItemForm.get('price').value,
    );
    this.newBillItemForm.reset();
    this.addBillItemEvent.emit(this.billItem);
  }

  onClearBillItem() {
    this.newBillItemForm.reset();
    this.pricePerUnit = undefined;
  }

  recalculatePricePerUnit() {
    this.calculatePricePerUnit(this.newBillItemForm.get('price').value,
      this.newBillItemForm.get('quantity').value);
  }

  private calculatePricePerUnit(price: number, quantity: number): void {
    if (price <=0 || quantity <= 0){
      this.pricePerUnit = undefined;
      return;
    }
    this.pricePerUnit = +(price / quantity).toFixed(2);
    this.logger.debug('recalculating price per unit ...');
    this.logger.debug('price:' + price);
    this.logger.debug('quantity:' + quantity);
    this.logger.debug('pricePerUnit:' + this.pricePerUnit);
  }


  static validateNegative(formControl: FormControl): {[s:string]: boolean} {
    return +formControl.value <= 0 ? {'negativeNumber' : true } : null;
  }
}

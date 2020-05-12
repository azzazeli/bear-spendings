import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BillItem} from '../../core/model/bill-item.model';
import {NGXLogger} from 'ngx-logger';
import {Product} from '../../core/model/product.model';
import {ProductsService} from '../../core/service/products.service';

@Component({
  selector: 'app-new-bill-item',
  templateUrl: './new-bill-item.component.html',
  styleUrls: ['./new-bill-item.component.css']
})
export class NewBillItemComponent implements OnInit {
  constructor(private logger: NGXLogger,
              private productService: ProductsService) { }
  newBillItemForm: FormGroup;

  @Output('add-bill-item')
  addBillItemEvent: EventEmitter<BillItem> = new EventEmitter();
  billItem: BillItem;
  pricePerUnit: number;
  totalPrice: number;
  productSuggestions: Product[];

  static validateNegative(formControl: FormControl): {[s: string]: boolean} {
    return +formControl.value <= 0 ? {'negativeNumber' : true } : null;
  }

  ngOnInit() {
    this.newBillItemForm = new FormGroup({
      'product': new FormControl(null, Validators.required),
      'price-per-unit': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative]),
      'quantity': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative])
    });
  }

  setBillItem(billItem: BillItem): void {
    this.logger.debug('NewBillItemComponent: Set bill item:' + JSON.stringify(billItem));
    this.billItem = billItem;
    this.newBillItemForm.get('product').setValue(new Product(billItem.productId, billItem.productName));
    this.newBillItemForm.get('price-per-unit').setValue(billItem.pricePerUnit);
    this.newBillItemForm.get('quantity').setValue(billItem.quantity);
    this.calculateTotalPrice(billItem.pricePerUnit, billItem.quantity);
  }

  reset(): void {
    this.newBillItemForm.reset();
    this.totalPrice = undefined;
  }

  searchProduct(event): void {
    this.logger.debug(`search product suggestions by:${event.query}`);
    this.productService.searchProductsBy(event.query).subscribe( (products: Product[]) => {
        this.productSuggestions = products;
    });
  }

  onAddBillItem(): void {
    this.logger.debug('NewBillItemComponent: On add bill item');
    const productControlValue = this.newBillItemForm.get('product').value;
    const productName = (productControlValue.name)
      ? productControlValue.name
      : productControlValue;
    this.recalculateTotalPrice();
    this.billItem = new BillItem(
      productControlValue.id,
      productName,
      this.newBillItemForm.get('price-per-unit').value,
      this.newBillItemForm.get('quantity').value,
      this.totalPrice
    );
    this.newBillItemForm.reset();
    this.addBillItemEvent.emit(this.billItem);
  }

  onClearBillItem() {
    this.reset();
  }

  recalculateTotalPrice() {
    this.logger.debug('recalculating total price ...');
    const pricePerUnit = this.newBillItemForm.get('price-per-unit').value;
    const quantity = this.newBillItemForm.get('quantity').value;
    this.calculateTotalPrice(pricePerUnit, quantity);
  }

  calculateTotalPrice(pricePerUnit: number, quantity: number) {
    this.logger.debug('calculating price per unit ...');
    this.totalPrice = +(pricePerUnit * quantity).toFixed(2);
    this.logger.debug(`total item price:${this.totalPrice}`);
  }

  productSelected(value) {
    this.logger.debug(`selected product:${JSON.stringify(value)} from suggestion. clean quantity and price-per-unit
     fields and total price`);
    this.totalPrice = undefined;
    this.newBillItemForm.get('price-per-unit').reset();
    this.newBillItemForm.get('quantity').reset();
  }
}

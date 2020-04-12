import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BillItem} from '../../core/model/bill-item.model';
import {NGXLogger} from 'ngx-logger';
import {Product} from "../../core/model/product.model";
import {ProductsService} from "../../core/service/products.service";

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
  productSuggestions: Product[];

  constructor(private logger: NGXLogger,
              private productService: ProductsService) { }

  ngOnInit() {
    this.newBillItemForm = new FormGroup({
      'product': new FormControl(null, Validators.required),
      'price': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative]),
      'quantity': new FormControl(null, [Validators.required, NewBillItemComponent.validateNegative])
    });
  }

  setBillItem(billItem: BillItem): void {
    this.logger.debug('NewBillItemComponent: Set bill item:' + JSON.stringify(billItem));
    this.billItem = billItem;
    this.newBillItemForm.get('product').setValue(new Product(billItem.productId, billItem.productName));
    this.newBillItemForm.get('price').setValue(billItem.price);
    this.newBillItemForm.get('quantity').setValue(billItem.quantity);
    this.calculatePricePerUnit(billItem.price, billItem.quantity);
  }

  reset(): void {
    this.newBillItemForm.reset();
    this.pricePerUnit = undefined;
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
    const productName = (productControlValue instanceof Product)
      ? productControlValue.name
      : productControlValue;
    this.billItem = new BillItem(
      productControlValue.id,
      productName,
      this.newBillItemForm.get('quantity').value,
      this.newBillItemForm.get('price').value,
    );
    this.newBillItemForm.reset();
    this.addBillItemEvent.emit(this.billItem);
  }

  onClearBillItem() {
    this.reset();
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

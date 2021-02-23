import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NewBillItemComponent} from './new-bill-item.component';
import {ReactiveFormsModule} from '@angular/forms';
import {BillItem} from '../../core/model/bill-item.model';
import {SamplesDataService} from '../../core/service/samplesDataService';
import {LoggerModule, NGXLogger, NgxLoggerLevel} from 'ngx-logger';
import {By} from '@angular/platform-browser';
import {DebugElement} from '@angular/core';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {Product} from '../../core/model/product.model';
import {ProductsService} from '../../core/service/products.service';
import {of} from 'rxjs';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('NewBillItemComponent', () => {
  let component: NewBillItemComponent;
  let fixture: ComponentFixture<NewBillItemComponent>;
  let samplesDataService: SamplesDataService;
  let productServiceSpy: jasmine.SpyObj<ProductsService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, AutoCompleteModule
        ,HttpClientTestingModule
         ,LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [
        SamplesDataService,
        NGXLogger,
        { provide: ProductsService, useValue: jasmine.createSpyObj('ProductService', ['searchProductsBy'])}
      ],
      declarations: [ NewBillItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBillItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    samplesDataService = TestBed.get(SamplesDataService);
    productServiceSpy = TestBed.get(ProductsService);
    productServiceSpy.searchProductsBy.and.callFake(function (productPrefix) {
      return of(samplesDataService.productsStartsWithCa());
      }
    );
  });

  it('#on select product from suggestion - clear price per unit and quantity', () => {
    const productInput: DebugElement = fixture.debugElement.query(By.css('#bill-item-product'));
    component.newBillItemForm.get('price-per-unit').setValue(23.2);
    component.newBillItemForm.get('quantity').setValue(2);

    productInput.triggerEventHandler('completeMethod', {'query': 'car'});
    productInput.triggerEventHandler('onSelect', {'id': 122, 'name': 'Carne'});
    expect(component.newBillItemForm.get('quantity').value).toBeNull();
    expect(component.newBillItemForm.get('price-per-unit').value).toBeNull();
    expect(component.totalPrice).toBeUndefined();
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
    expect(component.newBillItemForm).not.toBeNull();
    expect(component.newBillItemForm.get('product')).not.toBeNull();
    expect(component.newBillItemForm.get('price-per-unit')).not.toBeNull();
    expect(component.newBillItemForm.get('quantity')).not.toBeNull();
  });

  it('#validation checks', () => {
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('product').setValue('Chefir JLC 1.5%');
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('price-per-unit').setValue(7.88);
    expect(component.newBillItemForm.valid).toBe(false);
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('quantity').setValue(2);
    expect(component.newBillItemForm.valid).toBe(true);
  });

  it('#on add new bill new product name' , () => {
    // given
    const expected = {
      'product': 'Chefir JLC 2.5%',
      'price-per-unit': 9.55,
      'quantity': 2
    };
    component.newBillItemForm.setValue(expected);
    spyOn(component.addBillItemEvent, 'emit').and.callThrough();

    // then
    component.addBillItemEvent.subscribe((newBillItem: BillItem) => {
      expect(newBillItem.productId).toBeUndefined();
      expect(newBillItem.productName).toEqual(expected['product']);
      expect(newBillItem.pricePerUnit).toBe(expected['price-per-unit']);
      expect(newBillItem.quantity).toBe(expected.quantity);
      expect(newBillItem.totalPrice).toBe(19.10);
    });
    // when
    component.onAddBillItem();
  });

  it('#on add new bill fire event' , () => {
    // given
    const expected = {
      'product': new Product(1, 'Chefir JLC 2.5%'),
      'price-per-unit': 9.55,
      'quantity': 2
    };
    component.newBillItemForm.setValue(expected);
    spyOn(component.addBillItemEvent, 'emit').and.callThrough();

    // then
    component.addBillItemEvent.subscribe((newBillItem: BillItem) => {
      expect(newBillItem.productId).toEqual(expected['product'].id);
      expect(newBillItem.productName).toEqual(expected['product'].name);
      expect(newBillItem.pricePerUnit).toBe(expected['price-per-unit']);
      expect(newBillItem.quantity).toBe(expected.quantity);
      expect(newBillItem.totalPrice).toBe(19.10);
    });

    // when
    component.onAddBillItem();
    expect(component.addBillItemEvent.emit).toHaveBeenCalled();
  });

  it('#on set bill item - populate form with data and calculate total price', () => {
    // when
    const billItem = samplesDataService.sampleBillItem(1);
    component.setBillItem(billItem);
    fixture.detectChanges();
    // then
    expect(component.newBillItemForm.get('product').value).toEqual(new Product(billItem.productId, billItem.productName));
    expect(component.newBillItemForm.get('price-per-unit').value).toBe(billItem.pricePerUnit);
    expect(component.newBillItemForm.get('quantity').value).toBe(billItem.quantity);
    expect(component.totalPrice).toBe(40.62);
  });

  it('#on add bill item - disable add button', () => {
    const addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
    const billItem = samplesDataService.sampleBillItem(1);
    component.setBillItem(billItem);
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(false);
    component.onAddBillItem();
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
  });

  it('#on clear bill item - empty all inputs', () => {
    // given
    component.setBillItem(samplesDataService.sampleBillItem(1));
    // when
    component.onClearBillItem();
    // then
    expect(component.newBillItemForm.get('product').value).toBeNull();
    expect(component.newBillItemForm.get('quantity').value).toBeNull();
    expect(component.newBillItemForm.get('price-per-unit').value).toBeNull();
    expect(component.pricePerUnit).toBeUndefined();
  });

  it('#validate negative price', () => {
    const billItem = samplesDataService.sampleBillItem(1);
    billItem.pricePerUnit = -2;
    component.setBillItem(billItem);
    expect(component.newBillItemForm.valid).toBe(false);
    expect(component.newBillItemForm.controls['price-per-unit'].invalid).toBe(true);

    billItem.pricePerUnit = 2;
    component.setBillItem(billItem);
    expect(component.newBillItemForm.valid).toBe(true);
  });

  it('#validate negative quantity', () => {
    const billItem = samplesDataService.sampleBillItem(1);
    billItem.quantity = -2;
    component.setBillItem(billItem);
    expect(component.newBillItemForm.valid).toBe(false);
    expect(component.newBillItemForm.controls['quantity'].invalid).toBe(true);

    billItem.quantity = 2;
    component.setBillItem(billItem);
    expect(component.newBillItemForm.valid).toBe(true);
  });

  it('#on change price, quantity - recalculate total price', () => {
    const billItem = samplesDataService.sampleBillItem(1);
    component.setBillItem(billItem);
    const pricePerUnitField: DebugElement = fixture.debugElement.query(By.css('#bill-item-price-per-unit'));
    pricePerUnitField.nativeElement.value = '10.00';
    pricePerUnitField.nativeElement.dispatchEvent(new Event('change'));
    expect(component.totalPrice).toBe(20.00);
    const quantityField: DebugElement = fixture.debugElement.query(By.css('#bill-item-quantity'));
    quantityField.nativeElement.value = '3';
    quantityField.nativeElement.dispatchEvent(new Event('change'));
    expect(component.totalPrice).toBe(30.00);
  });

  it('#on price per unit or quantity = 0,total price per must be undefined or zero', () => {
    const billItem = samplesDataService.sampleBillItem(1);
    component.setBillItem(billItem);
    const pricePerUnitField: DebugElement = fixture.debugElement.query(By.css('#bill-item-price-per-unit'));
    pricePerUnitField.nativeElement.value = '0';
    pricePerUnitField.nativeElement.dispatchEvent(new Event('change'));
    expect(component.totalPrice).toEqual(0);

    const quantityField: DebugElement = fixture.debugElement.query(By.css('#bill-item-quantity'));
    quantityField.nativeElement.value = '0';
    quantityField.nativeElement.dispatchEvent(new Event('change'));
    expect(component.totalPrice).toEqual(0);
  });

  it('#search products ', () => {
    const productInput: DebugElement = fixture.debugElement.query(By.css('#bill-item-product'));
    productInput.triggerEventHandler('completeMethod', {'query': 'car'});
    expect(component.productSuggestions).toEqual([
      new Product(122, 'Carne'),
      new Product(123, 'Cartofi')
    ]);
  });

});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBillItemComponent } from './new-bill-item.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BillItem } from '../../core/model/bill-item.model';
import { SamplesDataService } from '../../core/service/samplesDataService';
import { LoggerModule, NGXLogger, NgxLoggerLevel } from 'ngx-logger';

describe('NewBillItemComponent', () => {
  let component: NewBillItemComponent;
  let fixture: ComponentFixture<NewBillItemComponent>;
  let samplesDataService: SamplesDataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
      providers: [SamplesDataService, NGXLogger],
      declarations: [ NewBillItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBillItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    samplesDataService = TestBed.get(SamplesDataService);
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
    expect(component.newBillItemForm).toBeDefined();
    expect(component.newBillItemForm.get('product-id')).toBeDefined();
    expect(component.newBillItemForm.get('product-name')).toBeDefined();
    expect(component.newBillItemForm.get('quantity')).toBeDefined();
    expect(component.newBillItemForm.get('price')).toBeDefined();
  });

  it('#validation checks', () => {
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('product-name').setValue('Chefir JLC 1.5%');
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('price').setValue(7.88);
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('quantity').setValue(2);
    expect(component.newBillItemForm.valid).toBe(true);
  });

  it('#on add new bill fire event' , () => {
    //given
    const expected = {
      'product-id': 1,
      'product-name': 'Chefir JLC 2.5%',
      'price': 9.55,
      'quantity': 2
    };
    component.newBillItemForm.setValue(expected);
    spyOn(component.addBillItemEvent, 'emit').and.callThrough();

    //then
    component.addBillItemEvent.subscribe((newBillItem: BillItem) => {
      expect(newBillItem.price).toBe(expected.price);
      expect(newBillItem.productId).toBe(expected['product-id']);
      expect(newBillItem.productName).toBe(expected['product-name']);
      expect(newBillItem.quantity).toBe(expected.quantity);
    });
    //when
    component.onAddBillItem();
    expect(component.addBillItemEvent.emit).toHaveBeenCalled();
  });

  it('#on set bill item - populate form with data', () => {
    //when
    const billItem = samplesDataService.sampleBillItem(1);
    component.setBillItem(billItem);
    fixture.detectChanges();
    //then
    expect(component.newBillItemForm.get('product-id').value).toBe(billItem.productId);
    expect(component.newBillItemForm.get('product-name').value).toBe(billItem.productName);
    expect(component.newBillItemForm.get('price').value).toBe(billItem.price);
    expect(component.newBillItemForm.get('quantity').value).toBe(billItem.quantity);
  });

  it('#on add bill item - disable add button', () => {
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
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
    //given
    component.setBillItem(samplesDataService.sampleBillItem(1));
    //when
    component.onClearBillItem();
    //then
    expect(component.newBillItemForm.get('product-id').value).toBeNull();
    expect(component.newBillItemForm.get('product-name').value).toBeNull();
    expect(component.newBillItemForm.get('price').value).toBeNull();
    expect(component.newBillItemForm.get('quantity').value).toBeNull();
  });

  it('#validate negative price', () => {
    const billItem = samplesDataService.sampleBillItem(1);
    billItem.price = -2;
    component.setBillItem(billItem);
    expect(component.newBillItemForm.valid).toBe(false);
    expect(component.newBillItemForm.controls['price'].invalid).toBe(true);

    billItem.price = 2;
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

});

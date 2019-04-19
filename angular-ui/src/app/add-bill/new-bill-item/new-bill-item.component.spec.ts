import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBillItemComponent } from './new-bill-item.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BillItem } from '../../core/model/bill-item.model';
import { TestService } from '../../core/service/test.service';
import { LoggerModule, NGXLogger, NgxLoggerLevel } from 'ngx-logger';

describe('NewBillItemComponent', () => {
  let component: NewBillItemComponent;
  let fixture: ComponentFixture<NewBillItemComponent>;
  let testService: TestService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
      providers: [TestService, NGXLogger],
      declarations: [ NewBillItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBillItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    testService = TestBed.get(TestService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.newBillItemForm).toBeDefined();
    expect(component.newBillItemForm.get('product-id')).toBeDefined();
    expect(component.newBillItemForm.get('product-name')).toBeDefined();
    expect(component.newBillItemForm.get('quantity')).toBeDefined();
    expect(component.newBillItemForm.get('price')).toBeDefined();
  });

  it('validation checks', () => {
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('product-name').setValue('Chefir JLC 1.5%');
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('price').setValue(7.88);
    expect(component.newBillItemForm.valid).toBe(false);
    component.newBillItemForm.get('quantity').setValue(2);
    expect(component.newBillItemForm.valid).toBe(true);
  });

  it('on add new bill fire event' , () => {
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

  it('on set bill item - populate form with data', () => {
    //when
    const billItem = testService.sampleBillItem(1);
    component.setBillItem(billItem);
    fixture.detectChanges();
    //then
    expect(component.newBillItemForm.get('product-id').value).toBe(billItem.productId);
    expect(component.newBillItemForm.get('product-name').value).toBe(billItem.productName);
    expect(component.newBillItemForm.get('price').value).toBe(billItem.price);
    expect(component.newBillItemForm.get('quantity').value).toBe(billItem.quantity);
  });

  it('on add bill item - disable add button', () => {
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
    const billItem = testService.sampleBillItem(1);
    component.setBillItem(billItem);
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(false);
    component.onAddBillItem();
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
  })

});

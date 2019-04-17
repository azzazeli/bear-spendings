import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBillComponent } from './add-bill.component';
import { FormArray, ReactiveFormsModule } from '@angular/forms';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { StoreService } from '../service/store.service';
import { Store } from '../model/store.model';
import { of } from 'rxjs/internal/observable/of';
import { CalendarModule } from 'primeng/primeng';

describe('AddBillComponent', () => {
  let component: AddBillComponent;
  let fixture: ComponentFixture<AddBillComponent>;
  let storeServiceSpy: jasmine.SpyObj<StoreService>;

  function sampleProduct(id: number) {
    return {
      'product-id': id,
      'product-name': 'Chefir JLC 2.5%',
      'quantity': 2,
      'price': 7.85
    };
  }

  function expectNewBillItemReset() {
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    expect(component.newBillItemForm.get('product-name').value).toBe(null);
    expect(component.newBillItemForm.get('product-id').value).toBe(null);
    expect(component.newBillItemForm.get('quantity').value).toBe(null);
    expect(component.newBillItemForm.get('price').value).toBe(null);
    expect(addToBillBtn.disabled).toBe(true);
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, CalendarModule, LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
      providers: [
        {provide: StoreService, useValue: jasmine.createSpyObj('StoreService', ['getStores'])}
      ],
      declarations: [AddBillComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddBillComponent);
    component = fixture.componentInstance;
    storeServiceSpy = TestBed.get(StoreService);
    storeServiceSpy.getStores.and.returnValue(of([new Store(1, 'Nr.1')]));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('on store select show top products', () => {
    expect(component.stores).toBeUndefined();
    expect(component.addBillForm).toBeUndefined();
    fixture.detectChanges();
    expect(component.stores).toBeDefined();
    expect(component.addBillForm).toBeDefined();
    expect(component.addBillForm.contains('store')).toEqual(true);
  });

  it('on select item from top product populate bill item', () => {
    expect(component.topStoreProducts).toBeUndefined();
    fixture.detectChanges();
    component.addBillForm.get('store').setValue(1);
    component.onStoreSelected();
    expect(component.topStoreProducts).toBeDefined();
  });

  it('on top product select populate bill item form', () => {
    //given
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
    component.addBillForm.get('store').setValue(1);
    component.onStoreSelected();

    //when
    component.onTopProductSelected(2);
    fixture.detectChanges();

    //then
    expect(component.newBillItemForm.get('product-id').value).toEqual(2);
    expect(component.newBillItemForm.get('product-name').value).toEqual('Chefir JLC 1.5%');
    expect(component.newBillItemForm.get('quantity').value).toEqual(1);
    expect(component.newBillItemForm.get('price').value).toEqual(7.85);

    expect(addToBillBtn.disabled).toBe(false);
  });

  it('on add bill item from top store products', () => {
    //given
    fixture.detectChanges();
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(0);
    component.newBillItemForm.setValue(sampleProduct(1));
    //when
    component.onAddBillItem();
    //then
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-id').value).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('quantity').value).toBe(2);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-name').value).toBe('Chefir JLC 2.5%');
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('price').value).toBe(7.85);

    //given
    component.newBillItemForm.setValue(sampleProduct(2));
    //when
    component.onAddBillItem();
    //then
    expect((<FormArray>component.addBillForm.get('bill-items')).at(1).get('product-id').value).toBe(2)
  });

  it(' on delete bill item - remove item from array form', ()=> {
    //given
    fixture.detectChanges();
    component.newBillItemForm.setValue(sampleProduct(1));
    component.onAddBillItem();
    component.newBillItemForm.setValue(sampleProduct(2));
    component.onAddBillItem();
    //when
    component.onDeleteBillItem(0);
    //then
    let billItems = <FormArray>component.addBillForm.get('bill-items');
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-id').value).toBe(2);
  });


  it('on add bill item clean new-bill item form, disable add bill item button, top store product has no selected item', ()=> {
    //given
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
    component.newBillItemForm.setValue(sampleProduct(1));
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(false);

    //when
    component.onAddBillItem();
    fixture.detectChanges();
    //then
    expectNewBillItemReset();
    expect(component.selectedProductId).toBe(null);
  });

  it('on store select reset bill-item form and selected top product', () => {
    //given
    fixture.detectChanges();
    //when
    component.onStoreSelected();
    //then
    expect(component.selectedProductId).toBe(null);
    expectNewBillItemReset();
  });

  it('enable \'add bill\' button when ' +
    '1. there at least on bill item ' +
    '2. store is selected ' +
    '3. date is selected', () => {
    //given
    fixture.detectChanges();
    let addBillBtn = fixture.nativeElement.querySelector('#add-bill-btn');
    expect(addBillBtn.disabled).toBe(true);
    expect(component.addBillForm.valid).toBe(false);
    //when
    component.addBillForm.get('bill-date').setValue('04/11/2019');
    component.addBillForm.get('store').setValue('1');
    component.newBillItemForm.setValue(sampleProduct(1));
    component.onAddBillItem();
    //then
    expect(component.addBillForm.valid).toBe(true)
  });

});

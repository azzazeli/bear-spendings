import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AddBillComponent} from './add-bill.component';
import {FormArray, ReactiveFormsModule} from '@angular/forms';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {StoreService} from '../core/service/store.service';
import {Store} from '../core/model/store.model';
import {of} from 'rxjs/internal/observable/of';
import {AutoCompleteModule, CalendarModule, MessageService} from 'primeng/primeng';
import {SamplesDataService} from '../core/service/samplesDataService';
import {NewBillItemComponent} from './new-bill-item/new-bill-item.component';
import {ProductsService} from '../core/service/products.service';
import {BillService} from '../core/service/bill.service';
import {Bill} from '../core/model/bill.model';
import * as moment from 'moment';
import {ToastModule} from 'primeng/toast';
import {StoreProduct} from '../core/model/store-product.model';
import {Product} from '../core/model/product.model';

describe('AddBillComponent', () => {
  let component: AddBillComponent;
  let fixture: ComponentFixture<AddBillComponent>;
  let storeServiceSpy: jasmine.SpyObj<StoreService>;
  let billServiceSpy: jasmine.SpyObj<BillService>;
  let productsServiceSpy: jasmine.SpyObj<ProductsService>;
  let samplesDataService: SamplesDataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule, CalendarModule, ToastModule, AutoCompleteModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
      providers: [
        MessageService,
        SamplesDataService,
        {provide: StoreService, useValue: jasmine.createSpyObj('StoreService', ['getStores'])},
        {provide: BillService, useValue: jasmine.createSpyObj('BillService', ['addBill'])},
        {provide: ProductsService, useValue: jasmine.createSpyObj('ProductsService',
            ['topStoreProducts', 'getObservableById'])}
      ],
      declarations: [AddBillComponent, NewBillItemComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddBillComponent);
    component = fixture.componentInstance;
    storeServiceSpy = TestBed.get(StoreService);
    productsServiceSpy = TestBed.get(ProductsService);
    billServiceSpy = TestBed.get(BillService);
    storeServiceSpy.getStores.and.returnValue(of([new Store(1, 'Nr.1')]));
    samplesDataService = TestBed.get(SamplesDataService);
    productsServiceSpy.topStoreProducts.and.returnValue(of(samplesDataService.sampleStoreProducts()));
    productsServiceSpy.getObservableById.and.callFake(function (productId) {
      const products: Product[] = samplesDataService.sampleProducts();
      return of(products.find(product => product.id == productId));
    });
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#on store select - show top products', () => {
    expect(component.stores).toBeUndefined();
    expect(component.addBillForm).toBeUndefined();
    fixture.detectChanges();
    expect(component.stores).toBeDefined();
    expect(component.addBillForm).toBeDefined();
    expect(component.addBillForm.contains('store-id')).toEqual(true);

    //when
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();
    //then
    expect(productsServiceSpy.topStoreProducts).toHaveBeenCalledWith(1);
  });

  it('#on select item from top product - populate bill item', () => {
    expect(component.topStoreProducts).toBeUndefined();
    fixture.detectChanges();
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();
    expect(component.topStoreProducts).toBeDefined();
  });

  it('#on top product select - populate bill item form', () => {
    //given
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    expect(addToBillBtn.disabled).toBe(true);
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();

    //when
    component.onTopProductSelected(2);
    fixture.detectChanges();

    //then
     const expectedStoreProduct:StoreProduct = samplesDataService.sampleStoreProducts()[1];
     expect(component.newBillItemComponent.billItem.productId).toEqual(expectedStoreProduct.productId);
     expect(component.newBillItemComponent.billItem.productName).toEqual(samplesDataService.sampleProducts()[1].name);
     expect(component.newBillItemComponent.billItem.quantity).toEqual(expectedStoreProduct.quantity);
     expect(component.newBillItemComponent.billItem.pricePerUnit).toEqual(expectedStoreProduct.price);

     expect(addToBillBtn.disabled).toBe(false);
  });

  it('#on add bill item from top store products', () => {
    //given
    fixture.detectChanges();
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(0);
    //when
    component.onAddBillItem(samplesDataService.sampleBillItem(1));
    //then
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-id').value).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('quantity').value).toBe(2);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-name').value).toBe('Chefir JLC 2.5%');
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('price').value).toBe(20.31);
    expect(component.billTotal).toEqual(20.31);

    //when
    component.onAddBillItem(samplesDataService.sampleBillItem(2));
    //then
    expect((<FormArray>component.addBillForm.get('bill-items')).at(1).get('product-id').value).toBe(2);
    expect(component.billTotal).toEqual(2 * 20.31);
  });

  it('#on delete bill item - remove item from array form', ()=> {
    // given
    fixture.detectChanges();
    component.onAddBillItem(samplesDataService.sampleBillItem(1));
    component.onAddBillItem(samplesDataService.sampleBillItem(2));
    // when
    component.onDeleteBillItem(0);
    // then
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-id').value).toBe(2);
    expect(component.billTotal).toEqual(20.31);
  });


  it('#on add bill item - top store product has no selected item', ()=> {
    //given
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    component.addBillForm.get('bill-date').setValue('04/11/2019');
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();
    //when
    component.onAddBillItem(samplesDataService.sampleBillItem(1));
    fixture.detectChanges();
    //then
    expect(component.addBillForm.valid).toBe(true);
    expect(component.selectedProductId).toBe(null);
    expect(addToBillBtn.disabled).toBe(true);
  });

  it('#on store select reset bill - item form and selected top product', () => {
    //given
    let addToBillBtn = fixture.nativeElement.querySelector('#add-to-bill-btn');
    fixture.detectChanges();
    //when
    component.onStoreSelected();
    //then
    expect(component.selectedProductId).toBe(null);
    expect(addToBillBtn.disabled).toBe(true);
  });

  it('#enable \'add bill\' button when ' +
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
    component.addBillForm.get('store-id').setValue('1');
    component.onAddBillItem(samplesDataService.sampleBillItem(1));
    //then
    expect(component.addBillForm.valid).toBe(true)
  });

  it('#on add bill - call BillService.addBill, reset new bill form', () => {
    //given
    let addedBill = new Bill(moment(new Date('2019-09-19T00:00:00')), 1);
    addedBill.id = 1224;
    billServiceSpy.addBill.and.returnValue(of(addedBill));
    fixture.detectChanges();
    component.addBillForm.get('bill-date').setValue(new Date('2019-09-19T00:00:00'));
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();
    component.onAddBillItem(samplesDataService.sampleBillItem(1));

    //when
    component.onAddBill();
    const expectedBill: Bill = new Bill(moment(component.normalizedDate(new Date('2019-09-19T00:00:00'))), 1);
    expectedBill.items.push(samplesDataService.sampleBillItem(1));
    expectedBill.total = (20.31);
    //then
    expect(billServiceSpy.addBill).toHaveBeenCalledWith(expectedBill);
    expectFormReset();
  });


  it('#on clear form click - expect form reset ', () => {
    fixture.detectChanges();
    // given
    component.addBillForm.get('bill-date').setValue(new Date('2019-09-19T00:00:00'));
    component.addBillForm.get('store-id').setValue(1);
    component.onStoreSelected();
    component.onAddBillItem(samplesDataService.sampleBillItem(1));
    // when
    component.onClearForm();
    // then
    expectFormReset();
  });

  it('bill date default value: today', () => {
    const before = moment();
    fixture.detectChanges();
    const after = component.addBillForm.get('bill-date').value.getTime();
    expect(before.isSameOrBefore(after)).toBeTruthy();
    expect(after).toBeGreaterThanOrEqual(before.toDate().getTime());
  });

  function expectFormReset () {
    const now = moment();
    expect(component.topStoreProducts.length).toBe(0);
    expect((<FormArray>component.addBillForm.get('bill-items')).controls).toEqual([]);
    const billDate = moment(component.addBillForm.get('bill-date').value);
    expect(billDate.isSame(now, 'minute')).toBeTruthy('Bill date must be today');
    expect(component.addBillForm.get('store-id').value).toBeNull('Store must be reset');
    expect(component.addBillForm.pristine).toBe(true);
    expect(component.addBillForm.touched).toBeFalsy();
    expect(component.selectedProductId).toBeNull('Selected top product must be reset');
    expect(component.billTotal).toEqual(0.0);
  }

});

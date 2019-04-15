import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBillComponent } from './add-bill.component';
import { FormArray, ReactiveFormsModule } from '@angular/forms';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { StoreService } from '../service/store.service';
import { Store } from '../model/store.model';
import { of } from 'rxjs/internal/observable/of';

describe('AddBillComponent', () => {
  let component: AddBillComponent;
  let fixture: ComponentFixture<AddBillComponent>;
  let storeServiceSpy: jasmine.SpyObj<StoreService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
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
    expect(component.addBillForm.get('new-bill-item.product-id').value).toEqual(2);
    expect(component.addBillForm.get('new-bill-item.product-name').value).toEqual('Chefir JLC 1.5%');
    expect(component.addBillForm.get('new-bill-item.quantity').value).toEqual(1);
    expect(component.addBillForm.get('new-bill-item.price').value).toEqual(7.85);

    expect(addToBillBtn.disabled).toBe(false);
  });

  it('on add bill item from top store products', () => {
    //given
    fixture.detectChanges();
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(0);
    component.addBillForm.get('new-bill-item').setValue({
      'product-id': 1,
      'product-name': 'Chefir JLC 2.5%',
      'quantity': 2,
      'price': 7.85
    });
    //when
    component.onAddBillItem();
    //then
    expect((<FormArray>component.addBillForm.get('bill-items')).length).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-id').value).toBe(1);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('quantity').value).toBe(2);
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('product-name').value).toBe('Chefir JLC 2.5%');
    expect((<FormArray>component.addBillForm.get('bill-items')).at(0).get('price').value).toBe(7.85);
  });
});

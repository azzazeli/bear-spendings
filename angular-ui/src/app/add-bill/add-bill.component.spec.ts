import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBillComponent } from './add-bill.component';
import { ReactiveFormsModule } from '@angular/forms';
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
      imports: [ReactiveFormsModule, LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG} )],
      providers: [
        {provide: StoreService, useValue: jasmine.createSpyObj('StoreService', ['getStores'])}
      ],
      declarations: [ AddBillComponent ]
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
    expect(component.addBillForm.contains("store")).toEqual(true);
  });

  it('on select item from top product populate bill item', () => {
    expect(component.topStoreProducts).toBeUndefined();
    fixture.detectChanges();
    component.addBillForm.setValue({'store': '1'});
    component.onStoreSelected();
    expect(component.topStoreProducts).toBeDefined();
  })
});

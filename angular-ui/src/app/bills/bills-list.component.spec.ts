import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BillsListComponent} from './bills-list.component';
import {BillService} from '../core/service/bill.service';
import {SamplesDataService} from '../core/service/samplesDataService';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {of} from 'rxjs/internal/observable/of';
import {TableModule} from 'primeng/table';
import {StoreService} from '../core/service/store.service';
import {ProductsService} from '../core/service/products.service';
import createSpyObj = jasmine.createSpyObj;

describe('BillsListComponent', () => {
  let component: BillsListComponent;
  let fixture: ComponentFixture<BillsListComponent>;
  let billServiceSpy: jasmine.SpyObj<BillService>;
  let storeServiceSpy: jasmine.SpyObj<StoreService>;
  let productServiceSpy: jasmine.SpyObj<ProductsService>;
  let samplesDataService: SamplesDataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[
        TableModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [
        SamplesDataService,
        {provide: BillService, useValue: createSpyObj('BillService', ['allBills', 'allBillsCount'])},
        {provide: StoreService, useValue: createSpyObj('StoreService', ['getObservableById'])},
        {provide: ProductsService, useValue: createSpyObj('ProductsService', ['getObservableById'])}
      ],
      declarations: [ BillsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillsListComponent);
    component = fixture.componentInstance;
    billServiceSpy = TestBed.get(BillService);
    storeServiceSpy = TestBed.get(StoreService);
    productServiceSpy = TestBed.get(ProductsService);
    samplesDataService = TestBed.get(SamplesDataService);

    billServiceSpy.allBills.and.returnValue(of([samplesDataService.sampleBill()]));
    billServiceSpy.allBillsCount.and.returnValue(of(20));
    storeServiceSpy.getObservableById.and.returnValue(of(samplesDataService.sampleStores()[0]));
    productServiceSpy.getObservableById.and.returnValue((of(samplesDataService.sampleProducts()[0])));
    fixture.detectChanges();
  });

  it('#should create', async() => {
    fixture.whenStable().then(() => {
      expect(component).toBeTruthy();
      expect(component.totalRecords).toEqual(20);
      expect(billServiceSpy.allBillsCount).toHaveBeenCalledTimes(1);
    });
  });

  it('#should init bills []', () => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const component = fixture.componentInstance;
      expect(component.bills).toBeDefined();
      expect(component.loading).toEqual(false);
      expect(billServiceSpy.allBills).toHaveBeenCalledWith(0, component.PAGE_SIZE);
    });
  });

});

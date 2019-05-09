import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BillsListComponent } from './bills-list.component';
import { BillService } from '../core/service/bill.service';
import { SamplesDataService } from '../core/service/samplesDataService';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { of } from 'rxjs/internal/observable/of';
import { TableModule } from 'primeng/table';
import { StoreService } from '../core/service/store.service';
import createSpyObj = jasmine.createSpyObj;

describe('BillsListComponent', () => {
  let component: BillsListComponent;
  let fixture: ComponentFixture<BillsListComponent>;
  let billServiceSpy: jasmine.SpyObj<BillService>;
  let storeServiceSpy: jasmine.SpyObj<StoreService>;
  let samplesDataService: SamplesDataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[
        TableModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [
        SamplesDataService,
        {provide: BillService, useValue: createSpyObj('BillService', ['allBills'])},
        {provide: StoreService, useValue: createSpyObj('StoreService', ['getStore'])}
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
    samplesDataService = TestBed.get(SamplesDataService);
    billServiceSpy.allBills.and.returnValue(of([samplesDataService.sampleBill()]));
    storeServiceSpy.getStore.and.returnValue(of(samplesDataService.sampleStores()[0]));
    fixture.detectChanges();
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#should init bills []', () => {
    expect(component.bills).toBeDefined();
    expect(component.bills[0].store).toEqual(samplesDataService.sampleStores()[0]);
  })
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BillsListComponent } from './bills-list.component';
import { BillService } from '../core/service/bill.service';
import { SamplesDataService } from '../core/service/samplesDataService';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { of } from 'rxjs/internal/observable/of';
import { TableModule } from 'primeng/table';
import createSpyObj = jasmine.createSpyObj;

describe('BillsListComponent', () => {
  let component: BillsListComponent;
  let fixture: ComponentFixture<BillsListComponent>;
  let billServiceSpy: jasmine.SpyObj<BillService>;
  let samplesDataService: SamplesDataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[
        TableModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [
        SamplesDataService,
        {provide: BillService, useValue: createSpyObj('BillService', ['allBills'])}
      ],
      declarations: [ BillsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillsListComponent);
    component = fixture.componentInstance;
    billServiceSpy = TestBed.get(BillService);
    samplesDataService = TestBed.get(SamplesDataService);
    billServiceSpy.allBills.and.returnValue(of([samplesDataService.sampleBill()]));
    fixture.detectChanges();
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#should init bills []', () => {
    expect(component.bills).toBeDefined();
  })
});

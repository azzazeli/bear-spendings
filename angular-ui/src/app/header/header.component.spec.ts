import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {LoggerModule, LoggerTestingModule, NgxLoggerLevel} from 'ngx-logger';
import {BillService} from '../core/service/bill.service';
import createSpyObj = jasmine.createSpyObj;
import {of} from 'rxjs/internal/observable/of';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let billServiceSpy: jasmine.SpyObj<BillService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [LoggerTestingModule],
      declarations: [ HeaderComponent ],
      providers: [
        {provide: BillService, useValue: createSpyObj('BillService', ['exportAll']) }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    billServiceSpy = TestBed.get(BillService);
    billServiceSpy.exportAll.and.returnValue(of());
    fixture.detectChanges();
  });

  it('#on should create', () => {
    expect(component).toBeTruthy();
  });

  it('#on export all', () => {
    component.exportAll();
    expect(billServiceSpy.exportAll).toHaveBeenCalled();
  });
});

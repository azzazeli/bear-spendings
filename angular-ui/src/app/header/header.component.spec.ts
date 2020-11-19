import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {BillService} from '../core/service/bill.service';
import createSpyObj = jasmine.createSpyObj;

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let billServiceSpy: jasmine.SpyObj<BillService>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})],
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
    fixture.detectChanges();
  });

  it('#on should create', () => {
    expect(component).toBeTruthy();
  });

  fit('#on export all', () => {
    component.exportAll();
    expect(this.billServiceSpy.exportAll).toHaveBeenCalled();
  });
});

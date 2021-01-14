import { async, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {BillService} from './core/service/bill.service';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      declarations: [
        AppComponent,
        HeaderComponent,
      ],
      providers: [BillService]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have app-header component`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;

    expect(app.title).toEqual('angular-ui');
  });

  //todo: app routing testing
});

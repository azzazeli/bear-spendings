import {BillService} from './bill.service';
import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController, TestRequest} from '@angular/common/http/testing';
import {SamplesDataService} from './samplesDataService';
import {HttpClient} from '@angular/common/http';
import {Bill} from '../model/bill.model';
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";

describe('BillServiceTest',()  => {
  let billService: BillService;
  let samplesDataService: SamplesDataService;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [BillService, SamplesDataService]
    });

    billService = TestBed.get(BillService);
    samplesDataService = TestBed.get(SamplesDataService);
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('#on add bill - sent post http request', () => {
    //when
    billService.addBill(samplesDataService.sampleBill()).subscribe(() => {});
    //then
    const req = httpTestingController.expectOne(billService.ADD_BILL_URL, 'call to add bill url');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(samplesDataService.sampleBill());
  });

  it('#on bills - sent http get request', () => {
    //when
    billService.allBills(0, 10).subscribe((bills: Bill[]) => {
      expect(bills.length).toEqual(1);
      expect(bills[0].storeId).toBe(samplesDataService.sampleStores()[0].id);
    });
    //then
    const testRequest: TestRequest = httpTestingController.expectOne(`${billService.ALL_BILLS_URL}?page=0&size=10`);
    testRequest.flush([samplesDataService.sampleBill()]);
    expect(testRequest.request.method).toEqual('GET');
    expect(testRequest.request.params.get('page')).toEqual('0');
    expect(testRequest.request.params.get('size')).toEqual('10');
  });

  it('#on allBillsCount - sent a http get request', () => {
    //given
    const totalRecords = 12;
    //when
    billService.allBillsCount().subscribe(value => expect(value).toEqual(totalRecords));
    //then
    const req: TestRequest = httpTestingController.expectOne(billService.ALL_BILLS_COUNT_URL, 'call to all bill count url');
    req.flush(totalRecords);
  });

});

import { BillService } from './bill.service';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SamplesDataService } from './samplesDataService';
import { HttpClient } from '@angular/common/http';


describe('BillServiceTest',()  => {
  let billService: BillService;
  let samplesDataService: SamplesDataService;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
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
    debugger;
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(samplesDataService.sampleBill());
  });


});

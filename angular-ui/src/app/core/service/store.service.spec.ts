import {TestBed} from '@angular/core/testing';
import {StoreService} from './store.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Store} from '../model/store.model';
import {SamplesDataService} from './samplesDataService';
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";

describe('StoreServiceTest', () => {

  let storeService: StoreService;
  let samplesDataService: SamplesDataService;
  let httpTestingController : HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [StoreService, SamplesDataService]
    });
    storeService = TestBed.get(StoreService);
    samplesDataService = TestBed.get(SamplesDataService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    // no more pending requests
    httpTestingController.verify();
  });

  it('#getStores', () =>{
    //when
    storeService.getStores().subscribe((stores: Store[]) => {
      //then
      expect(stores).toEqual(samplesDataService.sampleStores(), ''), fail;
    });
    const req = httpTestingController.expectOne(storeService.STORES_URL);
    expect(req.request.method).toEqual('GET');
    req.flush(samplesDataService.sampleStores());
  });


  it('#getStore by id', () => {
    //when
    storeService.getObservableById(1).subscribe( (store: Store) => {
      expect(store.id).toEqual(2);
    });
    const req = httpTestingController.expectOne(`${storeService.GET_ST0RE_URL}/1`);
    expect(req.request.method).toEqual('GET');
    req.flush(samplesDataService.sampleStores()[1]);

    storeService.getObservableById(1);
    httpTestingController.expectNone(storeService.GET_ST0RE_URL);
  });
});

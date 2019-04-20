import { TestBed } from '@angular/core/testing';
import { StoreService } from './store.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Store } from '../model/store.model';
import { SamplesDataService } from './samplesDataService';

describe('StoreServiceTest', () => {

  let storeService: StoreService;
  let samplesDataService: SamplesDataService;
  let httpTestingController : HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
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

  it('#getStored', () =>{
    //when
    storeService.getStores().subscribe((stores: Store[]) => {
      //then
      expect(stores).toEqual(samplesDataService.sampleStores(), ''), fail;
    });
    const req = httpTestingController.expectOne(storeService.storesUrl);
    expect(req.request.method).toEqual('GET');
    req.flush(samplesDataService.sampleStores());
  });

});

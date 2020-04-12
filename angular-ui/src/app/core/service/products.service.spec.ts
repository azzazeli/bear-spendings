import {ProductsService} from './products.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {SamplesDataService} from './samplesDataService';
import {TestBed} from '@angular/core/testing';
import {Product} from '../model/product.model';
import {HttpClient} from '@angular/common/http';
import {StoreProduct} from "../model/store-product.model";
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";
import {StoreService} from "./store.service";

describe('ProductsServiceTest', () => {
  let productService: ProductsService;
  let storeService: StoreService;
  let samplesDataService: SamplesDataService;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG})
      ],
      providers: [ProductsService, SamplesDataService, StoreService]
    });
    productService = TestBed.get(ProductsService);
    storeService = TestBed.get(StoreService);
    samplesDataService = TestBed.get(SamplesDataService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('#topStoreProducts', () => {
    const storeId = 1;
    productService.topStoreProducts(storeId).subscribe((products: StoreProduct[]) => {
      expect(products).toEqual(samplesDataService.sampleStoreProducts());
    });
    const req = httpTestingController.match((request) => {
      return request.url === `${storeService.STORES_URL}/${storeId}/${productService.TOP_PRODUCTS}` &&
        request.method == 'GET'
    });
    expect(req.length).toEqual(1);
    req[0].flush(samplesDataService.sampleStoreProducts());
  });

  it('#get product by id', () => {
    //when
    productService.getObservableById(1).subscribe((product: Product) => {
      expect(product.id).toEqual(1);
    });

    const req = httpTestingController.expectOne(productService.productUrl(1));
    expect(req.request.method).toEqual('GET');
    req.flush(samplesDataService.sampleProducts()[0]);

  });

  it('#search product by prefix', () => {
    //given
    const productPrefix = 'ca';
    //when
    productService.searchProductsBy(productPrefix).subscribe();
    //then
    const req = httpTestingController.expectOne(`api/products?startWith=${productPrefix}`);
    expect(req.request.method).toEqual('GET');

  });

});

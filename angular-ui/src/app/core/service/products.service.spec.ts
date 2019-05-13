import { ProductsService } from './products.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SamplesDataService } from './samplesDataService';
import { TestBed } from '@angular/core/testing';
import { Product } from '../model/product.model';
import { HttpClient } from '@angular/common/http';

describe('ProductsServiceTest', () => {
  let productService: ProductsService;
  let samplesDataService: SamplesDataService;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductsService, SamplesDataService]
    });
    productService = TestBed.get(ProductsService);
    samplesDataService = TestBed.get(SamplesDataService);
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('#topStoreProducts', () => {
    const storeId = 1;
    productService.topStoreProducts(storeId).subscribe((products: Product[]) => {
      expect(products).toEqual(samplesDataService.sampleProducts());
    });
    const req = httpTestingController.match((request) => {
      return request.url === productService.TOP_STORE_PRODUCTS_URL &&
        request.method == 'GET' &&
        request.params.get('storeId') == storeId.toString()
    });
    expect(req.length).toEqual(1);
    req[0].flush(samplesDataService.sampleProducts());
  });

  it('#get product by id', () => {
    //when
    productService.getObservableById(1).subscribe((product: Product) => {
      expect(product.id).toEqual(1);
    });

    const req = httpTestingController.expectOne(productService.productUrl(1));
    expect(req.request.method).toEqual('GET');
    req.flush(samplesDataService.sampleProducts()[0]);

    productService.getObservableById(1);
    httpTestingController.expectNone(productService.productUrl(1));
  });
});

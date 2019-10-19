import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Product} from '../model/product.model';
import {ObservableCacheService} from './observable.cache.service';
import {environment} from "../../../environments/environment";
import {StoreProduct} from "../model/store-product.model";
import {NGXLogger} from "ngx-logger";

@Injectable()
export class ProductsService extends ObservableCacheService<Product>{
  TOP_STORE_PRODUCTS_URL = `${environment.apiUrl}${environment.TOP_STORE_PRODUCTS_URL}`;
  private GET_PRODUCT_URL = `${environment.apiUrl}${environment.PRODUCT_URL}`;

  constructor(private http: HttpClient, protected logger: NGXLogger) {
    super(logger);
  }

  productUrl(id: number) {
    let url = `${this.GET_PRODUCT_URL}${id}`;
    if (environment.mock) {
      url += '.json';
    }
    return url;
  }

  topStoreProducts(storeId: number): Observable<StoreProduct[]> {
    return this.http.get<StoreProduct[]>(this.TOP_STORE_PRODUCTS_URL, {
      params: {
        'storeId': storeId.toString()
      }
    });
  }

  protected fetchObservable(id: number): Observable<Product> {
    this.logger.debug(`fetching product with id: ${id}`);
    return this.http.get<Product>(this.productUrl(id));
  }
}

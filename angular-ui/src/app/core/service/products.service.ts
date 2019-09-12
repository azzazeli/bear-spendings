import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Product} from '../model/product.model';
import {ObservableCacheService} from './observable.cache.service';
import {environment} from "../../../environments/environment";

@Injectable()
export class ProductsService extends ObservableCacheService<Product>{
  TOP_STORE_PRODUCTS_URL = `${environment.apiUrl}${environment.TOP_STORE_PRODUCTS_URL}`
  private GET_PRODUCT_URL = `assets/products/`;

  constructor(private http: HttpClient) {
    super();
  }

  productUrl(id: number) {
    return `${this.GET_PRODUCT_URL}${id}.json`;
  }

  topStoreProducts(storeId: number): Observable<Product[]> {
    return this.http.get<Product[]>(this.TOP_STORE_PRODUCTS_URL, {
      params: {
        'storeId': storeId.toString()
      }
    });
  }

  protected fetchObservable(id: number): Observable<Product> {
    return this.http.get<Product>(this.productUrl(id));
  }
}

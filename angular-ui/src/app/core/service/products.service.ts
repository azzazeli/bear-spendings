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
  TOP_PRODUCTS = `${environment.TOP_PRODUCTS}`;
  private PRODUCTS_URL = `${environment.apiUrl}${environment.PRODUCTS_URL}`;

  constructor(private http: HttpClient, protected logger: NGXLogger) {
    super(logger);
  }

  productUrl(id: number) {
    let url = `${this.PRODUCTS_URL}/${id}`;
    if (environment.mock) {
      url += '.json';
    }
    return url;
  }

  searchProductsBy(productNamePrefix: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.PRODUCTS_URL}?startWith=${productNamePrefix}`);
  }

  //todo: better place is in store service
  topStoreProducts(storeId: number): Observable<StoreProduct[]> {
    return this.http.get<StoreProduct[]>(`${environment.apiUrl}${environment.STORES_URL}/${storeId}/${this.TOP_PRODUCTS}`);
  }

  protected fetchObservable(id: number): Observable<Product> {
    this.logger.debug(`fetching product with id: ${id}`);
    return this.http.get<Product>(this.productUrl(id));
  }
}

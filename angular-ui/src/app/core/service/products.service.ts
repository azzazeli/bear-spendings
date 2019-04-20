import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../model/product.model';

@Injectable()
export class ProductsService {
  TOP_STORE_PRODUCTS_URL = 'assets/top-store-products.json';

  constructor(private http: HttpClient) {}

  topStoreProducts(storeId: number): Observable<Product[]> {
    return this.http.get<Product[]>(this.TOP_STORE_PRODUCTS_URL, {
      params: {
        'storeId': storeId.toString()
      }
    });
  }
}

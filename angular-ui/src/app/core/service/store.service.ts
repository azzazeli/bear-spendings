import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '../model/store.model';
import { Observable } from 'rxjs';

@Injectable()
export class StoreService {
  //todo: use `${environment.api}`
  STORES_URL: string = "assets/stores.json";
  GET_ST0RE_URL: string = 'assets/getStore.json';

  //todo: try RequestCache https://blog.fullstacktraining.com/caching-http-requests-with-angular/
  private observableCache: {[key: number]: Observable<Store>} = {};
  constructor(private http: HttpClient) {}

  getStores(): Observable<Store[]> {
    return this.http.get<Store[]>(this.STORES_URL);
  }

  getStore(id: number): Observable<Store> {
    if(!this.observableCache[id]) {
      this.observableCache[id] = this.fetchStore(id);
    }
    return this.observableCache[id];

    }

  fetchStore(id: number): Observable<Store> {
    return this.http.get<Store>(this.GET_ST0RE_URL);
  }

}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '../model/store.model';
import { Observable } from 'rxjs';
import { ObservableCacheService } from './observable.cache.service';

@Injectable()
export class StoreService extends ObservableCacheService<Store>{
  //todo: use `${environment.api}`
  STORES_URL: string = "assets/stores.json";
  GET_ST0RE_URL: string = 'assets/getStore.json';

  constructor(private http: HttpClient) {
    super();
  }

  getStores(): Observable<Store[]> {
    return this.http.get<Store[]>(this.STORES_URL);
  }

  protected fetchObservable(id: number): Observable<Store> {
    return this.http.get<Store>(this.GET_ST0RE_URL);
  }

}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Store} from '../model/store.model';
import {Observable} from 'rxjs';
import {ObservableCacheService} from './observable.cache.service';
import {environment} from "../../../environments/environment";

@Injectable()
export class StoreService extends ObservableCacheService<Store>{
  STORES_URL: string = `${environment.apiUrl}${environment.STORES_URL}`;
  GET_ST0RE_URL: string = `${environment.apiUrl}${environment.GET_ST0RE_URL}`;

  constructor(private http: HttpClient) {
    super();
  }

  getStores(): Observable<Store[]> {
    return this.http.get<Store[]>(this.STORES_URL);
  }

  protected fetchObservable(id: number): Observable<Store> {
    return this.http.get<Store>(`${this.GET_ST0RE_URL}/${id}`);
  }

}

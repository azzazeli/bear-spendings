import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Store} from '../model/store.model';
import {Observable} from 'rxjs';
import {ObservableCacheService} from './observable.cache.service';
import {environment} from "../../../environments/environment";
import {NGXLogger} from "ngx-logger";

@Injectable()
export class StoreService extends ObservableCacheService<Store>{
  STORES_URL: string = `${environment.apiUrl}${environment.STORES_URL}`;

  constructor(private http: HttpClient, protected logger: NGXLogger) {
    super(logger);
  }

  getStores(): Observable<Store[]> {
    this.logger.debug('getting all store');
    return this.http.get<Store[]>(this.STORES_URL);
  }

  protected fetchObservable(id: number): Observable<Store> {
    this.logger.debug(`fetching store with id:${id}`);
    return this.http.get<Store>(`${this.STORES_URL}/${id}`);
  }

}

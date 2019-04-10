import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '../model/store.model';
import { Observable } from 'rxjs';

@Injectable()
export class StoreService {

  constructor(private http: HttpClient) {}

  getStores(): Observable<Store[]> {
    return this.http.get<Store[]>("assets/stores.json");
  }
}

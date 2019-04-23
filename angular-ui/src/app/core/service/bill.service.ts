import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Bill } from '../model/bill.model';
import { Observable } from 'rxjs';

@Injectable()
export class BillService {

  ADD_BILL_URL ='call_server_url';

  constructor(private http: HttpClient) {}

  public addBill(bill: Bill): Observable<Bill> {
    return this.http.post<Bill>(this.ADD_BILL_URL, bill);
  }
}

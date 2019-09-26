import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Bill} from '../model/bill.model';
import {Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import {NGXLogger} from "ngx-logger";

@Injectable()
export class BillService {

  ADD_BILL_URL = `${environment.apiUrl}add_bill`;
  ALL_BILLS_URL = `${environment.apiUrl}${environment.ALL_BILLS_URL}`;
  ALL_BILLS_COUNT_URL = `${environment.apiUrl}${environment.ALL_BILLS_URL}/count`;

  constructor(private http: HttpClient, private logger: NGXLogger) {}

  public addBill(bill: Bill): Observable<Bill> {
    return this.http.post<Bill>(this.ADD_BILL_URL, bill);
  }

  public allBillsCount(): Observable<number> {
    return this.http.get<number>(this.ALL_BILLS_COUNT_URL);
  }

  public allBills(page: number, size: number): Observable<Bill[]> {
    this.logger.debug("environment.apiUrl", environment.apiUrl);
    return this.http.get<Bill[]>(this.ALL_BILLS_URL, {
      params: {
        'page': page.toString(),
        'size': size.toString()
      }
    });
  }
}

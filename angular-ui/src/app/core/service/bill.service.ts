import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Bill} from '../model/bill.model';
import {Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import {NGXLogger} from "ngx-logger";

@Injectable()
export class BillService {

  ADD_BILL_URL ='call_server_url';
  ALL_BILLS_URL = environment.apiUrl+environment.ALL_BILLS_URL;

  constructor(private http: HttpClient, private logger: NGXLogger) {}

  public addBill(bill: Bill): Observable<Bill> {
    return this.http.post<Bill>(this.ADD_BILL_URL, bill);
  }

  public allBills(): Observable<Bill[]> {
    this.logger.debug("mock environment.apiUrl", environment.apiUrl);
    return this.http.get<Bill[]>(this.ALL_BILLS_URL);
  }
}

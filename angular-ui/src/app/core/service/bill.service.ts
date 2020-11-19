import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Bill} from '../model/bill.model';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {NGXLogger} from 'ngx-logger';

@Injectable()
export class BillService {

  BILLS_URL = `${environment.apiUrl}${environment.BILLS_URL}`;
  ALL_BILLS_COUNT_URL = `${environment.apiUrl}${environment.BILLS_URL}/count`;

  constructor(private http: HttpClient, private logger: NGXLogger) {
    this.logger.debug('environment.apiUrl:', environment.apiUrl);
  }

  public addBill(bill: Bill): Observable<Bill> {
    return this.http.post<Bill>(this.BILLS_URL, bill);
  }

  public allBillsCount(): Observable<number> {
    return this.http.get<number>(this.ALL_BILLS_COUNT_URL);
  }

  public allBills(page: number, size: number): Observable<Bill[]> {
    this.logger.debug(`all bills url:${this.BILLS_URL}`);
    return this.http.get<Bill[]>(this.BILLS_URL, {
      params: {
        'page': page.toString(),
        'size': size.toString()
      }
    });
  }

  public exportAll(): Observable<any> {
    this.logger.debug('exporting all bills ...');
    return this.http.get('export-all', {responseType: 'blob'});
  }
}

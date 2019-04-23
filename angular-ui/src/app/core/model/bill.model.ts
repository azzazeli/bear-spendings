import { BillItem } from './bill-item.model';
import * as moment from 'moment';

export class Bill {
  public id: number;
  public purchaseDate: moment.Moment;
  public storeId: number;
  public billItems: BillItem[] = [];

  constructor(purchaseDate: moment.Moment, storeId: number) {
    this.purchaseDate = purchaseDate;
    this.storeId = storeId;
  }
}

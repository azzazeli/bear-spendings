import {BillItem} from './bill-item.model';
import * as moment from 'moment';
import {Store} from './store.model';

export class Bill {
  public id: number;
  public orderDate: moment.Moment;
  public storeId: number;
  public store: Store;
  public items: BillItem[] = [];

  constructor(orderDate: moment.Moment, storeId: number) {
    this.orderDate = orderDate;
    this.storeId = storeId;
  }
}

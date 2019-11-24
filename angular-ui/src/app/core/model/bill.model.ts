import {BillItem} from './bill-item.model';
import * as moment from 'moment';
import {Store} from './store.model';

export class Bill {
  public id: number;
  //todo: review - this field may be Date; it is string when bill comes from server
  // from UI moment is normalized to utc for correct work of JSON.stringify()
  public orderDate: moment.Moment;
  public storeId: number;
  public store: Store;
  public items: BillItem[] = [];
  public total: number;

  constructor(orderDate: moment.Moment, storeId: number) {
    this.orderDate = orderDate;
    this.storeId = storeId;
  }
}

import {BillItem} from '../model/bill-item.model';
import {Store} from '../model/store.model';
import {Product} from '../model/product.model';
import {Bill} from '../model/bill.model';
import * as moment from 'moment';
import {StoreProduct} from "../model/store-product.model";

export class SamplesDataService {

  sampleBillItem(id: number): BillItem {
    return new BillItem(
      id, 'Chefir JLC 2.5%', 1, 9.85
    );
  }

  sampleStores(): Store[] {
    return [
      new Store(1, 'Nr.1'),
      new Store(2, 'Pegas'),
      new Store(3, 'Fourchette'),
      new Store(4, 'Ali Market')
    ];
  }

  sampleProducts(): Product[] {
    return [
      new Product(1, 'Piine de secara', 12),
      new Product(2, 'Chefir JLC 2.5 %', 8.55),
      new Product(3, 'Unt BioButter', 78),
      new Product(4, 'Pesmeti', 10),
      new Product(5, 'Brinza Casuta mea', 18),
      new Product(6, 'Cascaval Dor Blue', 37.4),
      new Product(7, 'Cacaval Masdam', 44.6),
      new Product(8, 'Masline mari', 33.1),
      new Product(9, 'Banane', 33),
      new Product(10, 'Lamie', 21)
    ];
  }

  sampleStoreProducts(): StoreProduct[] {
    return [
      new StoreProduct(1, 2, 12),
      new StoreProduct(2, 3, 8.55)
    ]
  }

  sampleBill(): Bill {
    const bill: Bill = new Bill(moment('2019-04-21'), this.sampleStores()[0].id);
    bill.items = [
      this.sampleBillItem(1),
      this.sampleBillItem(2)
    ];
    return bill;
  }
}

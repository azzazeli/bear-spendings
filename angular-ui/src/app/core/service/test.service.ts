import { BillItem } from '../model/bill-item.model';

export class TestService {

  sampleBillItem(id: number): BillItem {
    return new BillItem(
      id, 'Chefir JLC 2.5%', 1, 9.85
    );
  }
}

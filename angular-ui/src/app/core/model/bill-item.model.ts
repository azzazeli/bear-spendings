
export class BillItem {
  public id: number;
  public productId: number;
  public productName: string;
  public quantity: number;
  public price: number;

  constructor(productId: number, productName: string, quantity: number, price: number) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.price = price;
  }
}

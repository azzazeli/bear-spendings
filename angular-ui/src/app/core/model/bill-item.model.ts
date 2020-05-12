
export class BillItem {
  public id: number;
  public productId: number;
  public productName: string;
  public quantity: number;
  public pricePerUnit: number;
  public totalPrice: number;
  constructor(productId: number, productName: string,
              pricePerUnit: number,
              quantity: number,
              totalPrice?: number) {
    this.productId = productId;
    this.productName = productName;
    this.pricePerUnit = pricePerUnit;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }
}

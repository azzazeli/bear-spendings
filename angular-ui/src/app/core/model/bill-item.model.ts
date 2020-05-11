
export class BillItem {
  public id: number;
  public productId: number;
  public productName: string;
  public quantity: number;
  public pricePerUnit: number;
  public totalPrice: number;
  constructor(productId: number, productName: string,
              quantity: number,
              pricePerUnit: number,
              totalPrice?: number) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.pricePerUnit = pricePerUnit;
    this.totalPrice = totalPrice;
  }
}

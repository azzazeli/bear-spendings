
export class StoreProduct {

  public productId: number;
  public quantity: number;
  public price: number;

  constructor(productId: number, quantity: number, price: number) {
    this.productId = productId;
    this.quantity = quantity;
    this.price = price;
  }

}


export class Product {
  public id: number;
  public name: string;
  //?? todo: should be StoreProduct entity with product Id and price
  public price: number;

  constructor(id: number, name: string, price: number) {
    this.id = id;
    this.name = name;
    this.price = price;
  }
}

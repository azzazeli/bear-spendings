
insert into store (id, city, country, location, name) values (1, 'chisinau', 'moldova', 'botanica viaduct', 'nr.1');

insert into product(id, name) values (1, 'chefir jlc 1%');
insert into product(id, name) values (2, 'chefir jlc 2.5%');
insert into product(id, name) values (3, 'Inghetata Beriozka 1kg');

insert into bill(id, order_date, store_id) values (1, '2019-04-18', 1);
insert into bill(id, order_date, store_id) values (2, '2019-04-22', 1);

insert into bill_item(id, price, quantity, product_id, bill_id) values (1, 7.85, 1, 1, 1);
insert into bill_item(id, price, quantity, product_id, bill_id) values (2, 9.85, 1, 2, 1);

insert into bill_item(id, price, quantity, product_id, bill_id) values (3, 87.00, 1, 3, 2);
insert into bill_item(id, price, quantity, product_id, bill_id) values (4, 10.00, 1, 2, 2);

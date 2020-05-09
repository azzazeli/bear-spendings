
insert into store (id, city, country, location, name) values (1, 'chisinau', 'moldova', 'botanica viaduct', 'Nr.1');

insert into product(id, name) values (1, 'chefir jlc 1%');
insert into product(id, name) values (2, 'chefir jlc 2.5%');
insert into product(id, name) values (3, 'Inghetata Beriozka 1kg');

insert into bill(id, order_date, store_id, total) values (1, '2019-04-18', 1, 21.0);
insert into bill(id, order_date, store_id, total) values (2, '2019-04-22', 1, 97.00);
insert into bill(id, order_date, store_id, total) values (3, '2019-04-22', 1, 230.00);

-- bill 2
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (1, 7.85, 1, 7.85, 1, 1);
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (2, 9.85, 1, 9.85, 2, 1);

--bill 3
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (3, 87.00, 1, 87.00, 3, 2);
insert into bill_item(id, price_per_unit, quantity, total_price,product_id, bill_id) values (4, 10.00, 1, 10.00, 2, 2);

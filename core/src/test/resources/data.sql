
insert into unit_of_measure(id, name) values (1, 'unit');
insert into store (id, city, country, location, name) values (1, 'chisinau', 'moldova', '', 'DEFAULT');

insert into category(id, parent_id, name) values (1, null,  'Food & Drinks');
insert into category(id, parent_id, name) values (2, 1, 'Lactate');
insert into category(id, parent_id, name) values (3, 1, 'Inghetata');

insert into product(id, name, unit_id, category_id) values (1, 'chefir jlc 1%', 1, 2);
insert into product(id, name, unit_id, category_id) values (2, 'chefir jlc 2.5%', 1, 2);
insert into product(id, name, unit_id, category_id) values (3, 'Inghetata Beriozka 1kg', 1, 3);
insert into product(id, name, unit_id, category_id) values (4, 'Brinzica', 1, 2);

insert into bill(id, order_date, store_id, total) values (1, '2019-04-18', 1, 21.0);
insert into bill(id, order_date, store_id, total) values (2, '2019-04-22', 1, 97.00);
insert into bill(id, order_date, store_id, total) values (3, '2019-04-22', 1, 230.00);

-- bill 1
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (1, 7.85, 1, 7.85, 1, 1);
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (2, 9.85, 1, 9.85, 2, 1);

--bill 2
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (3, 87.00, 1, 87.00, 3, 2);
insert into bill_item(id, price_per_unit, quantity, total_price,product_id, bill_id) values (4, 10.00, 1, 10.00, 2, 2);

--bill 3
insert into bill_item(id, price_per_unit, quantity, total_price, product_id, bill_id) values (5, 9.00, 2, 18.00, 1, 3);

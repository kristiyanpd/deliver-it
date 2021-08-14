INSERT INTO `countries` (`country_id`, `country_name`)
VALUES (1, 'Bulgaria'),
       (2, 'Germany'),
       (3, 'France'),
       (4, 'Italy');

INSERT INTO `cities` (`city_id`, `city_name`, `country_id`)
VALUES (1, 'Varna', 1),
       (2, 'Haskovo', 1),
       (3, 'Sofia', 1),
       (4, 'Berlin', 2),
       (5, 'Paris', 3),
       (6, 'Rome', 4),
       (7, 'Pleven', 1);

INSERT INTO `addresses` (`address_id`, `street_name`, `city_id`)
VALUES (1, 'Byala mura', 1),
       (2, 'Tsar Samuil', 2),
       (3, 'Pirotska 5', 3),
       (4, 'Leopoldstraße 50', 4),
       (5, '70 rue Nationale', 5),
       (6, ' Vicolo Tre Marchetti 100', 6),
       (7, 'ul. Otec Paisii 11', 3),
       (8, 'ul. 8 Dekemvri', 3),
       (9, 'Strada Starale', 6),
       (10, 'ul. Tsar Simeon', 7),
       (11, 'Eschenweg 128', 4),
       (12, '88 Faubourg Saint Honoré', 5);

INSERT INTO `roles` (`role_id`, `name`)
VALUES (1, 'Customer'),
       (2, 'Employee');

INSERT INTO `warehouses` (`warehouse_id`, `address_id`)
VALUES (1, 3),
       (2, 4),
       (3, 5),
       (4, 6);

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email`, `address_id`, `role_id`)
VALUES (1, 'Krisiyan', 'Dimitrov', 'kristiyan.dimitrov@gmail.com', 1, 2),
       (2, 'Kamen', 'Georgiev', 'georgiev.kameng@gmail.com', 2, 2),
       (3, 'Petar', 'Raykov', 'petar.raykov@gmail.com', 8, 1),
       (4, 'Todor', 'Andonov', 'todor.andonov@gmail.com', 8, 1),
       (5, 'Vladimir', 'Venkov', 'vladimir.venkov@gmail.com', 8, 1),
       (6, 'Franko', 'Machiavelli', 'franko@gmail.com', 9, 1),
       (7, 'Petranka', 'Stoilkova', 'petranka.s@gmail.com', 10, 1),
       (8, 'Rudolf', 'Weber', 'rudolf.webers@gmail.com', 11, 1),
       (9, 'Julien', 'Bonfils', 'juien.bonfils@gmail.com', 12, 1);

INSERT INTO `shipments` (`is_full`, `shipment_id`, `departure_date`, `arrival_date`, `status`, `origin_warehouse_id`,
                         `destination_warehouse_id`)
VALUES (0, 1, '2021-08-10', '2021-08-20', 'ON_THE_WAY', 2, 3),
       (1, 2, '2021-12-14', '2022-01-14', 'PREPARING', 4, 1),
       (0, 3, '2021-08-20', '2021-08-24', 'PREPARING', 1, 2),
       (0, 4, '2021-03-14', '2021-07-14', 'COMPLETED', 1, 3),
       (1, 5, '2021-07-14', '2021-10-14', 'ON_THE_WAY', 2, 1),
       (0, 6, '2021-08-16', '2021-08-17', 'ON_THE_WAY', 4, 1);

INSERT INTO `parcels` (`parcel_id`, `weight`, `category`, `pick_up_option`, `user_id`, `shipment_id`)
VALUES (1, 5, 'ELECTRONICS', 'DELIVER_TO_ADDRESS', 4, 1),
       (2, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 5, 1),
       (3, 15, 'ELECTRONICS', 'PICK_UP_FROM_WAREHOUSE', 3, 2),
       (4, 3, 'CLOTHING', 'DELIVER_TO_ADDRESS', 7, 6),
       (5, 4, 'CLOTHING', 'DELIVER_TO_ADDRESS', 5, 6),
       (6, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 6, 5),
       (7, 1.5, 'ELECTRONICS', 'PICK_UP_FROM_WAREHOUSE', 8, 3),
       (8, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 8, 3),
       (9, 5, 'CLOTHING', 'PICK_UP_FROM_WAREHOUSE', 9, 4),
       (10, 5, 'CLOTHING', 'DELIVER_TO_ADDRESS', 9, 4);
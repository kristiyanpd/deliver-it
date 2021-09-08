INSERT INTO `countries` (`country_id`, `country_name`) VALUES
	(1, 'Bulgaria'),
	(2, 'Germany'),
	(3, 'France'),
	(4, 'Italy');

INSERT INTO `cities` (`city_id`, `city_name`, `country_id`) VALUES
	(1, 'Varna', 1),
	(2, 'Haskovo', 1),
	(3, 'Sofia', 1),
	(4, 'Berlin', 2),
	(5, 'Paris', 3),
	(6, 'Rome', 4),
	(7, 'Pleven', 1),
	(9, 'test', 1);

INSERT INTO `addresses` (`address_id`, `street_name`, `city_id`) VALUES
	(1, 'Byala mura', 1),
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
	(12, '88 Faubourg Saint Honoré', 5),
	(13, 'Достава до офис на Speedy', 1),
	(14, 'Бяла мура 20', 1),
	(15, 'test', 1),
	(16, '8 декември', 3),
	(17, 'Vuzrajdane', 1),
	(18, 'test', 4),
	(19, 'City Centre', 4),
	(20, 'Centre', 1),
	(21, 'Varna Centre', 1),
	(22, 'Център', 1),
	(23, 'Бяла мура 202', 1),
	(24, 'bul Bulgaria', 3);

INSERT INTO `roles` (`role_id`, `name`) VALUES
	(1, 'Customer'),
	(2, 'Employee');
	
INSERT INTO `warehouses` (`warehouse_id`, `address_id`, `photo_url`) VALUES
	(1, 8, 'https://www.novinite.com/media/images/2019-06/photo_verybig_197568.jpg'),
	(2, 4, 'https://m.foolcdn.com/media/millionacres/original_images/warehouse_building.jpg?crop=16:9,smart'),
	(3, 2, 'https://cdna.artstation.com/p/assets/images/images/038/009/028/large/iam_vb-warehouse-exterior-dock-view.jpg?1621933265'),
	(4, 6, 'https://www.pv-tech.org/wp-content/uploads/2021/04/csm_BayWa_r.e._New_warehouse_Netherlands_April_1920_x_1080_42779cccf3.jpeg');
	
INSERT INTO `users` (`user_id`, `email`, `password`, `first_name`, `last_name`, `address_id`, `profile_picture`, `role_id`) VALUES
	(1, 'kristiyan.dimitrov@gmail.com', 'testpw', 'Krisiyan', 'Dimitrov', 1, NULL, 2),
	(2, 'georgiev.kameng@gmail.com', 'testpw', 'Kamen', 'Georgiev', 2, 'https://scontent-sof1-1.xx.fbcdn.net/v/t1.6435-9/71304753_2418945268153165_2600200259292364800_n.jpg?_nc_cat=104&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=wijVC2DAvbMAX9JFCri&_nc_ht=scontent-sof1-1.xx&oh=26e02a0291521bb8d8d0d548c3e0ed13&oe=615EE048', 2),
	(3, 'petar.raykov@gmail.com', 'testpw', 'Petar', 'Raykov', 8, NULL, 1),
	(4, 'todor.andonov@gmail.com', 'testpw', 'Todor', 'Andonov', 8, NULL, 1),
	(5, 'vladimir.venkov@gmail.com', 'testpw', 'Vladimir', 'Venkov', 8, NULL, 1),
	(6, 'franko@gmail.com', 'testpw', 'Franko', 'Machiavelli', 9, NULL, 1),
	(7, 'petranka.s@gmail.com', 'testpw', 'Petranka', 'Stoilkova', 10, NULL, 1),
	(8, 'rudolf.webers@gmail.com', 'testpw', 'Rudolf', 'Weber', 11, NULL, 1),
	(9, 'juien.bonfils@gmail.com', 'testpw', 'Julien', 'Bonfils', 12, NULL, 1),
	(12, 'kristiyanpd02@gmail.com', '123321', 'Кристиян', 'Димитров', 14, 'https://scontent-sof1-1.xx.fbcdn.net/v/t1.6435-9/194331800_821509332102351_7186769120970548205_n.jpg?_nc_cat=108&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=Ixaa3BCP854AX-Y9aC8&_nc_ht=scontent-sof1-1.xx&oh=504061cebf2a77c749c0bc5897b16b01&oe=615E5D54', 2),
	(20, 'stoyan.petrov@gmail.com', '123321', 'Стоян', 'Петров', 24, 'https://previews.123rf.com/images/shekularaz/shekularaz1608/shekularaz160800014/60979026-mops-pug-dog-animal-dressed-up-in-navy-blue-suit-with-red-tie-business-man-vector-illustration-.jpg', 1);

INSERT INTO `shipments` (`is_full`, `shipment_id`, `departure_date`, `arrival_date`, `status`, `origin_warehouse_id`, `destination_warehouse_id`) VALUES
	(0, 1, '2021-08-10', '2021-08-20', 'ON_THE_WAY', 2, 3),
	(0, 2, '2021-12-14', '2022-01-14', 'PREPARING', 2, 1),
	(1, 3, '2021-08-20', '2021-08-25', 'PREPARING', 1, 4),
	(0, 4, '2021-03-14', '2021-07-14', 'COMPLETED', 1, 3),
	(1, 5, '2021-07-14', '2021-10-15', 'PREPARING', 2, 1),
	(0, 6, '2021-08-16', '2021-08-17', 'ON_THE_WAY', 4, 1),
	(0, 11, '2021-08-31', '2021-09-10', 'COMPLETED', 1, 4),
	(0, 12, '2021-08-03', '2021-08-26', 'COMPLETED', 1, 2),
	(0, 13, '2021-08-03', '2021-09-10', 'ON_THE_WAY', 1, 3),
	(0, 16, '2021-09-08', '2021-09-15', 'PREPARING', 1, 2);

INSERT INTO `parcels` (`parcel_id`, `weight`, `category`, `pick_up_option`, `user_id`, `shipment_id`) VALUES
	(1, 5, 'ELECTRONICS', 'DELIVER_TO_ADDRESS', 4, 1),
	(2, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 5, 1),
	(3, 15, 'ELECTRONICS', 'PICK_UP_FROM_WAREHOUSE', 3, 12),
	(4, 3, 'CLOTHING', 'DELIVER_TO_ADDRESS', 7, 6),
	(5, 4, 'CLOTHING', 'PICK_UP_FROM_WAREHOUSE', 5, 6),
	(6, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 6, 5),
	(7, 1.5, 'ELECTRONICS', 'DELIVER_TO_ADDRESS', 8, 6),
	(8, 2, 'CLOTHING', 'DELIVER_TO_ADDRESS', 8, 3),
	(9, 5, 'CLOTHING', 'DELIVER_TO_ADDRESS', 9, 4),
	(10, 5, 'CLOTHING', 'DELIVER_TO_ADDRESS', 9, 4),
	(11, 5, 'ELECTRONICS', 'PICK_UP_FROM_WAREHOUSE', 9, 4),
	(12, 7, 'MEDICAL', 'PICK_UP_FROM_WAREHOUSE', 7, 4),
	(13, 5.74, 'CLOTHING', 'DELIVER_TO_ADDRESS', 1, 13),
	(14, 3, 'ELECTRONICS', 'DELIVER_TO_ADDRESS', 9, 11),
	(15, 5, 'ELECTRONICS', 'DELIVER_TO_ADDRESS', 20, 2);

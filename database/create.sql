create table countries
(
    country_id   int auto_increment
        primary key,
    country_name varchar(50) not null
);

create table cities
(
    city_id    int auto_increment
        primary key,
    city_name  varchar(50) not null,
    country_id int         not null,
    constraint cities_countries_fk
        foreign key (country_id) references countries (country_id)
);

create table addresses
(
    address_id  int auto_increment
        primary key,
    street_name varchar(100) not null,
    city_id     int          not null,
    constraint addresses_cities_fk
        foreign key (city_id) references cities (city_id)
);

create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(50) not null
);

create table users
(
    user_id    int auto_increment
        primary key,
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null,
    email      varchar(100) not null,
    address_id int          null
);

create table users_roles
(
    user_id int null,
    role_id int null,
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table warehouses
(
    warehouse_id int auto_increment
        primary key,
    address_id   int not null,
    constraint warehouses_addresses_fk
        foreign key (address_id) references addresses (address_id)
);

create table shipments
(
    shipment_id              int auto_increment
        primary key,
    departure_date           date                                          not null,
    arrival_date             date                                          not null,
    status                   enum ('ON_THE_WAY', 'PREPARING', 'COMPLETED') not null,
    origin_warehouse_id      int                                           not null,
    destination_warehouse_id int                                           not null,
    constraint shipments_destination_warehouse_fk
        foreign key (destination_warehouse_id) references warehouses (warehouse_id),
    constraint shipments_origin_warehouse_fk
        foreign key (origin_warehouse_id) references warehouses (warehouse_id)
);

create table parcels
(
    parcel_id      int auto_increment
        primary key,
    weight         double                                                not null,
    category       enum ('ELECTRONICS', 'CLOTHING', 'MEDICAL')           not null,
    pick_up_option enum ('PICK_UP_FROM_WAREHOUSE', 'DELIVER_TO_ADDRESS') not null,
    user_id        int                                                   not null,
    shipment_id    int                                                   not null,
    constraint parcels_shipments_fk
        foreign key (shipment_id) references shipments (shipment_id),
    constraint parcels_users_fk
        foreign key (user_id) references users (user_id)
);

create index parcels_categories_fk
    on parcels (category);

create index parcels_customers_fk
    on parcels (user_id);

create index shipments_statuses_fk
    on shipments (status);


create table client
(
    id   bigserial not null primary key,
    name varchar(50) not null,
    age  int4
);

create table account
(
    no   varchar(50) not null primary key,
    type varchar(50),
    rest real
);


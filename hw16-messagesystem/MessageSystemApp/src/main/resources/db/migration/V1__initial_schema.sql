create table client
(
    id       bigserial          not null primary key,
    login    varchar(10) unique not null,
    password varchar(20)        not null,
    name     varchar(50)        not null,
    age      int4
);

insert into client (login, password, name, age)
values ('admin', 'admin', 'AdMan', 100500);
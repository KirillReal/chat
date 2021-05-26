create table person
(
    id       serial primary key not null,
    login    varchar(1000),
    password varchar(1000),
    role_id serial
);

insert into person(login, password, role_id)
values ('admin', 'admin', 2);
insert into person(login, password, role_id)
values ('user', 'user', 1);

create table room
(
    id   serial primary key not null,
    name varchar(1000)
);

insert into room(name)
values ('hello room');
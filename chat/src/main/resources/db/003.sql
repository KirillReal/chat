create table role (
                      id serial primary key not null,
                      name varchar(100)
);

insert into role(id,name) values(1, 'ROLE_USER');
insert into role(id,name) values(2, 'ROLE_ADMIN');
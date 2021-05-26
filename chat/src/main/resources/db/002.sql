create table message
(
    id      serial primary key not null,
    text    text,
    time    timestamp,
    person_id serial,
    room_id serial
);

insert into message(text, time, person_id, room_id)
values ('Hello, world!', now(), 1, 1);
insert into message(text, time, person_id, room_id)
values ('Hello!', now(), 2, 1);

select person0_.id as id1_1_0_,
       person0_.login as login2_1_0_,
       person0_.password as password3_1_0_,
       person0_.role_id as role_id4_1_0_,
       role1_.id as id1_2_1_,
       role1_.name as name2_2_1_
from person person0_
         left outer join role role1_
                         on person0_.role_id=role1_.id
where person0_.id=?
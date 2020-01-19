insert into usr (id, active, username , password)
values (2, true, 'admin', '123');

insert into user_role(user_id, roles)
VALUES (1, 'USER'),
       (1, 'ADMIN');
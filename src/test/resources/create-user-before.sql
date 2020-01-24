delete from user_role;
delete from usr;

insert into usr(id,active,username,password) values
    (1,true, 'admin', '$2a$08$JO4XreI6gkRMIRi1INh/c.54UiP5yeSHa9ivqjKDqIRxhyY/gIOOq'),
    (2,true, 'Alex', '$2a$08$JO4XreI6gkRMIRi1INh/c.54UiP5yeSHa9ivqjKDqIRxhyY/gIOOq');

insert into user_role(user_id, roles) values
    (1,'USER'),(1,'ADMIN'),
    (2,'USER');
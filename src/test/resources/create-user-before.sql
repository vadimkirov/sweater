delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true,'$2a$08$AX.iv/8pXbVg.t6FsZX9p.bx4trhA708fQvsmrLgayldCpq23WCW.', 'admin'),
(2, true, '$2a$08$AX.iv/8pXbVg.t6FsZX9p.bx4trhA708fQvsmrLgayldCpq23WCW.', 'mike');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');
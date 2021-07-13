insert into author (name) values ('John Tolkien');
insert into author (name) values ('Michael Scott');

insert into book (title, creation_date, isbn, copies_total, rented_copies)
values ('Lord of the Rings - The Fellowship of the Ring', '1945-07-29', '11111111111111', 2, 1);

insert into book (title, creation_date, isbn, copies_total, rented_copies)
values ('Lord of the Rings - The Two Towers', '1954-11-11', '2222222222222', 20, 0);

insert into book (title, creation_date, isbn, copies_total, rented_copies)
values ('Somehow I Manage', '2006-03-03', '3333333333333', 10, 0);

insert into book_author(book_id, author_id) values (1, 1);
insert into book_author(book_id, author_id) values (2, 1);
insert into book_author(book_id, author_id) values (3, 2);

insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_READER');

insert into user (name, last_name, email, password) values ('Pera', 'Peric', 'pera@gmail.com', '$2a$10$IRhCLs3fmJVCCM28f14d7e9syofKktYBS/mVDsNui3OoMyW7vfNdm'); --admin
insert into user (name, last_name, email, password) values ('Mika', 'Mikic', 'mika@gmail.com', '$2a$10$TDjVh2IyqQ5MmLH.lPrwTegILRwxLoVpme8rEJSjAkZika79dYGTu'); --reader
insert into user (name, last_name, email, password) values ('User', 'User', 'user@gmail.com', '$2a$10$TDjVh2IyqQ5MmLH.lPrwTegILRwxLoVpme8rEJSjAkZika79dYGTu'); --reader

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (2,2);
insert into user_role(user_id, role_id) values (3,2);

insert into book_rental (date_of_renting, returned_date, user_id, book_id)
values ('2021-05-10', '2021-05-28', 2, 2 );

insert into book_rental (date_of_renting, returned_date, user_id, book_id)
values ('2021-05-01', null, 2, 2 );

insert into book_rental (date_of_renting, returned_date, user_id, book_id)
values ('2021-05-01', null, 2, 1 );


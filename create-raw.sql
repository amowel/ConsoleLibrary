create table author (id  bigserial not null, name varchar(255), primary key (id))
create table book (id  bigserial not null, added bytea, title varchar(255) not null, author_id int8, primary key (id))
alter table author add constraint UK_or6k6jmywerxbme223c988bmg unique (name)
alter table book add constraint UKtmh40lmm8oe6mhwv2stvywe9t unique (title, author_id)
alter table book add constraint FKklnrv3weler2ftkweewlky958 foreign key (author_id) references author

create table IF NOT EXISTS roles(
    id serial not null primary key,
    name varchar(255),
    created_at timestamp,
    updated_at timestamp
);
insert into roles(name, created_at, updated_at) values ('ADMIN', now(), now());
insert into roles(name, created_at, updated_at) values ('USER', now(), now());

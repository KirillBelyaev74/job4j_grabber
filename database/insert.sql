create table post
(
    id      serial primary key not null,
    name    varchar(50)        not null,
    text    text,
    link    text unique,
    created date
);
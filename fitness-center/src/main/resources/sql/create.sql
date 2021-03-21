create type EntityType as enum('User');

create type TurnstileEventType as enum ('ENTER', 'EXIT');

create table Keys (
    entity EntityType not null,
    maxId bigint not null,
    constraint pk_Keys primary key (entity)
);

insert into Keys values('User', 0);

create table Users (
    userId bigint not null,
    name varchar(150) not null,
    constraint pk_Users primary key (userId)
);

create table TurnstileEvents (
    userId bigint not null,
    eventId bigint not null,
    eventType TurnstileEventType not null,
    eventTime timestamp not null,
    constraint pk_TurnstileEvents primary key (userId, eventId),
    constraint fk_Users foreign key (userId) references Users(userId)
);

create table SubscriptionEvents (
    userId bigint not null,
    eventId bigint not null,
    endTime timestamp not null,
    constraint pk_SubscriptionEvents primary key (userId, eventId),
    constraint fk_Users foreign key (userId) references Users(userId)
);
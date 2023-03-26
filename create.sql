create table account (
   id bigint not null,
    current_balance decimal(38,2),
    initial_balance decimal(38,2),
    primary key (id)
) engine=InnoDB;

create table budget (
   id bigint not null auto_increment,
    goal_amount decimal(38,2),
    name varchar(255),
    period varchar(255),
    primary key (id)
) engine=InnoDB;

create table contact_msg (
   contact_id integer not null,
    created_at datetime(6),
    created_by varchar(255),
    update_at datetime(6),
    updated_by varchar(255),
    email varchar(255),
    message varchar(255),
    mobile_num varchar(255),
    name varchar(255),
    status varchar(255),
    subject varchar(255),
    primary key (contact_id)
) engine=InnoDB;

create table contact_msg_seq (
   next_val bigint
) engine=InnoDB;

insert into contact_msg_seq values ( 1 );

create table credential (
   id bigint not null,
    password varchar(255),
    username varchar(255),
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table credential_seq (
   next_val bigint
) engine=InnoDB;

insert into credential_seq values ( 1 );

create table ifinaces_keys (
   PK_NAME varchar(255) not null,
    PK_VALUE bigint,
    primary key (PK_NAME)
) engine=InnoDB;

insert into ifinaces_keys(PK_NAME, PK_VALUE) values ('account',0);

create table user (
   id bigint not null,
    user_address_line_1 varchar(255),
    address_line_2 varchar(255),
    city varchar(255),
    state varchar(255),
    zip_code varchar(255),
    last_name varchar(255),
    first_name varchar(255),
    primary key (id)
) engine=InnoDB;

create table user_account (
   account_id bigint not null,
    user_id bigint not null,
    primary key (account_id, user_id)
) engine=InnoDB;

create table user_seq (
   next_val bigint
) engine=InnoDB;

insert into user_seq values ( 1 );

alter table credential 
   add constraint FKpg7bdnqxpyhrt7f8soul9y7ne 
   foreign key (user_id) 
   references user (id);

alter table user_account 
   add constraint FK4qaqge5ewvmfuwsp5eddfr4r2 
   foreign key (user_id) 
   references user (id);

alter table user_account 
   add constraint FK1pxs5fj6ujqfs13gmfg3o5cwo 
   foreign key (account_id) 
   references account (id);

alter table credential 
   drop 
   foreign key FKpg7bdnqxpyhrt7f8soul9y7ne;

alter table user_account 
   drop 
   foreign key FK4qaqge5ewvmfuwsp5eddfr4r2;

alter table user_account 
   drop 
   foreign key FK1pxs5fj6ujqfs13gmfg3o5cwo;

drop table if exists account;

drop table if exists budget;

drop table if exists credential;

drop table if exists credential_seq;

drop table if exists ifinaces_keys;

drop table if exists user;

drop table if exists user_account;

drop table if exists user_seq;

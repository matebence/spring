### Code snippets

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

```sql
create database spring;
use spring;
create table user (user_id int, user_name varchar(30), user_pwd varchar(100), user_role varchar(20), user_enabled int);

INSERT INTO user values(501,'admin','$2a$10$ZMvqN2ZtqR6JCTqUYTSq9u41iDmhrUC10fFzWiG0UP9s7Nl9WJp4W','ROLE_ADMIN',1);
INSERT INTO user values(502,'employee','$2a$10$/1mVz4TShikyjxoKihcZjubWfM083QCwn/nowfPeaS5AqXPyMNZ4C','ROLE_EMPLOYEE',1);
INSERT INTO user values(503,'manager','$2a$10$NMIsSf40rIQxfcU0pdhTNOx3AFEM18B7.mU1o9dVXcs6lDLPzLa0y','ROLE_MANAGER',1);
```

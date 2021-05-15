###### To generate key

```
keytool -genkeypair -alias jwtiscool -keyalg RSA -keypass jwtiscool -keystore jwtiscool.jks -storepass jwtiscool
```

###### To view the key

```
 keytool -list -rfc --keystore jwtiscool.jks | openssl x509 -inform pem -pubkey
```

##### SQL Commands

###### Initial Setup

```
use mydb;

create table product(
id int AUTO_INCREMENT PRIMARY KEY,
name varchar(20),
description varchar(100),
price decimal(8,3) 
);

create table coupon(
id int AUTO_INCREMENT PRIMARY KEY,
code varchar(20) UNIQUE,
discount decimal(8,3),
exp_date varchar(100) 
);

```

###### Security tables

```
use mydb;

CREATE TABLE USER 
(
ID INT NOT NULL AUTO_INCREMENT,
FIRST_NAME VARCHAR(20),
LAST_NAME VARCHAR(20),
EMAIL VARCHAR(20),
PASSWORD VARCHAR(256), 
PRIMARY KEY (ID),
UNIQUE KEY (EMAIL)
);

CREATE TABLE ROLE 
(
ID INT NOT NULL AUTO_INCREMENT,
NAME VARCHAR(20),
PRIMARY KEY (ID)
);

CREATE TABLE USER_ROLE(
USER_ID int,
ROLE_ID int,
FOREIGN KEY (user_id)
REFERENCES user(id),
FOREIGN KEY (role_id)
REFERENCES role(id)
);

insert into user(first_name,last_name,email,password) values ('doug','bailey','doug@bailey.com','$2a$10$U2STWqktwFbvPPsfblVeIuy11vQ1S/0LYLeXQf1ZL0cMXc9HuTEA2');
insert into user(first_name,last_name,email,password) values ('john','ferguson','john@ferguson.com','$2a$10$YzcbPL.fnzbWndjEcRkDmO1E4vOvyVYP5kLsJvtZnR1f8nlXjvq/G');

insert into role values(1,'ROLE_ADMIN');
insert into role values(2,'ROLE_USER');

insert into user_role values(1,1);
insert into user_role values(2,2);

select * from user;
select * from role;
select * from user_role;

```

###### Token tables

```
use mydb;

create table oauth_access_token (
token_id varchar(255) NOT NULL,
token blob,
authentication_id varchar(255) DEFAULT NULL, 
user_name varchar(255) DEFAULT NULL, 
client_id varchar(255) DEFAULT NULL, 
authentication blob,
refresh_token varchar(255) DEFAULT NULL, 
PRIMARY KEY (token_id));

create table oauth_refresh_token ( 
token_id varchar(255) NOT NULL,
token blob,
authentication blob,
PRIMARY KEY (token_id));

select * from oauth_access_token;

select * from oauth_refresh_token;
```



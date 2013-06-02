---- define yammer group for application

create table yammer_group(
group_id  number not null,
name varchar2(24) not null,
table_name varchar2(30) not null,
creation_date  date default sysdate not null,
last_modified_date date default sysdate not null);


---- yammer group data

insert into yammer_group(GROUP_ID,
NAME,
TABLE_NAME,
CREATION_DATE,
LAST_MODIFIED_DATE) VALUES (1883168,'PayPal Stage Down','yammer_msg',sysdate, sysdate);
commit;


---- yammer_msg data

create table yammer_msg(
msg_id number not null,
partition_key number(2) default to_number(mod(to_char(sysdate,'MM'),12)) not null ,
stage_name varchar2(64),
text  varchar2(512),
client_type number(1),
created_time date,
parent_msg_id number,
send_id  number,
liked_count number,
CONSTRAINT yammer_msg_PK PRIMARY KEY (msg_id, partition_key)
using index online local)
partition by range (partition_key)
(partition 
part0
values less than (2),
PARTITION
PART1
VALUES LESS THAN (3),
PARTITION
PART2
VALUES LESS THAN (4),
PARTITION
PART3
VALUES LESS THAN (5),
PARTITION
PART4
VALUES LESS THAN (6),
PARTITION
PART5
VALUES LESS THAN (7),
PARTITION
PART6
VALUES LESS THAN (8),
PARTITION
PART7
VALUES LESS THAN (9),
PARTITION
PART8
VALUES LESS THAN (10),
PARTITION
PART9
VALUES LESS THAN (11),
PARTITION
PART10
VALUES LESS THAN (12),
PARTITION
PART11
VALUES LESS THAN (13),
PARTITION
PARTMAX
VALUES LESS THAN (MAXVALUE)
);



create sequence yammer_msg_seq;


---- yammer_msg_sentiment
create table yammer_msg_sentiment(
msg_id number not null,
sentiment_flag number(2),
positive_count number,
negetive_count number,
neutral_count number);

create table etl_process (
table_name varchar2(30) not null,
group_id number  default 0 not null,
last_process_id number not null,
last_process_time date not null
);

create unique index etl_process_udx on etl_process (table_name,group_id);

insert into etl_process (table_name, group_id,
last_process_id, last_process_time) values ('yammer_msg', 1883168, 0, sysdate);
commit;



create or replace view vw_yammer_daily as 
select to_date(to_char(created_time-0.24,'YYYY-MM-DD'),'YYYY-MM-DD') message_time,nvl(stage_name,'N/A') stage_Name, send_id, client_type,  count(*) msg_count from yammer_msg 
where message_time < to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD') 
group by  to_date(to_char(created_time-0.24,'YYYY-MM-DD'),'YYYY-MM-DD'), nvl(stage_name,'N/A'), send_id,client_type

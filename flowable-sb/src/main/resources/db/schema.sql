create table persion
(
	id bigint(20) not null comment '主键id',
	username varchar(30) not null unique comment '姓名',
	age int(11) null default null comment '年龄',
	email varchar(50)  default '' comment '邮箱',
    brith_date datetime  null comment '生日',
	primary key (id)
);
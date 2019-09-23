# shop-sql
0. 初始化数据库
```sql
create database db_shop default character set utf8;
use db_shop
```

1. 用户表
```sql
create table t_user (
	user_id bigint auto_increment,
	user_name varchar(255) not null,
	password varchar(255) not null,
	mobile varchar(11) not null,
	status int not null default 0 comment '0未激活 1正常 2冻结',
	primary key (user_id),
	unique (user_name)
);
```

2. 商品类目表
```sql
create table t_category (
	category_id bigint auto_increment,
	name varchar(255) not null,
	create_time datetime not null,
	primary key (category_id)
);
```

3. 商品表
```sql
create table t_goods (
	goods_id bigint auto_increment,
	name varchar(255) not null,
	price decimal(8,2) not null,
	stock int not null,
	status int not null default 0 comment '0正常 1已下架(stock=0或删除)',
	create_time datetime not null,
	category_id bigint not null,
	primary key (goods_id)
);
```

4. 订单详情表
```sql
create table t_order_item (
	order_item_id bigint auto_increment,
	order_id bigint not null,
	goods_id bigint not null,
	count int not null comment '商品数量',
	amount decimal(8,2) not null comment '订单详情总额',
	primary key (order_item_id)
);
```

5. 订单表
```sql
create table t_order (
	order_id bigint not null auto_increment,
	order_no varchar(255) not null comment '订单流水号',
	total decimal(8,2) not null comment '订单总额',
	status int not null default 0 comment '0未发货 1已发货 2已完成 3已取消 4已关闭',
	user_id bigint not null,
	primary key (order_id),
	unique (order_no)
);
```

6. 管理员表
```sql
create table t_admin (
	admin_id bigint not null auto_increment,
	admin_name varchar(255) not null,
	password varchar(255) not null,
	primary key (admin_id)
);
```
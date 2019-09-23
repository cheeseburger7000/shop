# shop

## redis
- 进入redis容器
```
docker  exec -it myredis  redis-cli
```

## `t_goods`增加image字段
```sql
alter table t_goods add image varchar(255) not null;

alter table t_order add create_time datetime not null;

alter table t_admin add root tinyint not null default 0;
```



## 七牛云图片上传


## 自动化测试


## gitflow


## 待处理
mybatis分页偏移问题
# shop
技术栈：
- Spring Boot + Mybatis
- Redis存储购物车，缓存
- Swagger2.x

## redis
- 进入redis容器
```
docker  exec -it myredis  redis-cli
```

## `t_goods`增加image字段
```sql
alter table t_goods add image varchar(255) not null;
```

## 七牛云图片上传

## 待处理
mybatis分页偏移问题

# 消息队列案例场景
- sms
- 同步索引库
- 更新缓存
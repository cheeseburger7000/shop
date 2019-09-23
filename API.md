# shop-api
## 一.用户模块
### 1. 注册
加盐加密
### 2. 登录
### 3. 用户列表 
要求：按用户状态降序
### 4. 更改用户状态

## 二.分类模块
### 1. 创建分类
### 2. 分类列表 
要求：按创建时间降序

## 三.商品模块
前台-商品服务 后台-商品管理

### 1. 商品列表 
要求：按在架状态升序，创建时间降序
### 2. 根据分类id获取商品列表
### 3. 根据商品id获取商品
### 4. 搜索商品 
要求：模糊查询。按在架状态升序，创建时间降序
### 5. 添加商品
### 6. 修改商品库存
### 7. 修改商品分类
### 8. 下架商品

## 四.购物车管理
前台-购物车服务 4+1+1

### 1. 根据用户id获取购物车 ok
参数：userId
返回值：cart

### 2. 添加商品到购物车 ok
要求：判断商品是否下架，下架就抛异常，提示商品已下架。
参数：userId, goodsId
返回值：cart

### 3. 删除商品到购物车 ok
参数：userId, goodsId
返回值：cart

### 4. 更改购物车商品数量
要求：注意边界值，即(0, 最大库存量]
参数：userId, goodsId
返回值：cart

### 5. 清空购物车 ok
参数：userId
返回值：cart

### 6. 删除商品项 ok 
参数：userId, goodsId
返回值：cart

## 五.订单模块 
前台-订单服务 后台-订单管理

### 1.创建订单 ok 
前台

要求：
1）创建订单成功之后，清空购物车
2）购物车存在下架商品时，抛出异常，返回前端订单创建失败，并提示用户删除商品之后继续操作。

### 2.取消订单 ok 
前台
### 3.根据用户id查询订单列表 ok
前台、后台
### 4.根据订单号查询订单详情 ok 
前台、后台
### 5.管理员发货 ok
后台
### 6.用户收货 ok
前台
### 7.确认取消 ok
后台
### 8.订单列表 ok
后台
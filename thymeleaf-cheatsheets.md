# thymeleaf-cheatsheets
前置内容：
- Spring Boot 自动配置原理
- 注解Enable*原理

##  自动配置
- ThymeleafAutoConfiguration
    - ThymeleafViewResolver
    - `@EnableConfigurationProperties(ThymeleafProperties.class)`

## html模板的前缀和后缀
查看 ThymeleafProperties 发现...
```
public static final String DEFAULT_PREFIX = "classpath:/templates/";

public static final String DEFAULT_SUFFIX = ".html"; 
```

## 关闭thymeleaf缓存（实用）
效果：修改html模板之后，按`Ctrl + Shift + F9`刷新工程，无需重启项目。
```yml
spring:
  thymeleaf:
    cache: false
```

## 语法
### 1.变量
- ognl表达式 `${user.name}`或 `${user['name']}`
- 指令或html5自定义属性 `th:text` 
    - 动静结合
    - `th:text` 会格式化输出html，若想输出原始内容应该使用 `th:utext`
- 自定义变量，举例： `th:object="${user}"` 和 `*{name}`
- 方法调用，内置对象

### 2.两个重点 
条件和循环怎么做？
- 循环 
    - `th:each="user : ${users}"` 
    - `th:each="user,status : ${users}"`
        - status包括：index，count，size，current，even/odd，first/last
- 条件
    - `th:if="${user.age} < 24"`
    - `th:unless`
    - true判定
 
###3.js模板
```
<script th:inline="javascript">
    const user = /*[[${user}]]*/ {};
    const age = /*[[${user.age}]]*/ 20;
    console.log(user);
    console.log(age)
</script>
```

## 其它
- 支持swagger
- api能够与SSR共存吗
- Spring Boot 整合 Dubbo
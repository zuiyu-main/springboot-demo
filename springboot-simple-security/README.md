## 官网security demo

二次加入了cat监控测试

[cat参考链接](http://www.iigrowing.cn/cat_tong_yi_jian_kong_ping_tai_jian_dan_shi_yong.html)

[github](https://github.com/dianping/cat/tree/master/lib/java)

[jar包参考链接](https://www.cnblogs.com/huanchupkblog/p/10687680.html)

## 登录记住我

* sql 

org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl#CREATE_TABLE_SQL

```sql
create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
```
## 参考文件

[文章地址](https://mp.weixin.qq.com/s?__biz=MzIwNzYzODIxMw==&tempkey=MTA4Nl9rSkkvWVdXbHNqMm4xSWZhRUxKbnhabFU4ZnVQVUJuYnY4TTMyV29CbEk5OG9tOHJWOVdhRGFnM1BVU2lUYU1CRjRwVllCakl0bDhIeGdRNXdfWm1DQU1Kb244YW1pRTBMc09JV3VQTTRvLWMtTEpkTlBWQW0ySVBESTN2dV9LTm85NmJoZHhJb3A5cGhzLW84NkQxSWQyeW83QUdYZHF2cG9KMTF3fn4%3D&chksm=170e1af2207993e44f8b167200fc184c712dbf08cb5cf16bf557747d942cb0e7c9b6b39bb6ae#rd)


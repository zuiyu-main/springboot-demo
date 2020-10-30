## 官网security demo
二次加入了cat监控测试
[cat参考链接](http://www.iigrowing.cn/cat_tong_yi_jian_kong_ping_tai_jian_dan_shi_yong.html)
[github](https://github.com/dianping/cat/tree/master/lib/java)
[jar包参考链接](https://www.cnblogs.com/huanchupkblog/p/10687680.html)

## 登录记住我
* sql JdbcTokenRepositoryImpl

```sql
create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
```



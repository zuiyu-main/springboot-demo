# mybatis-plus
[官网](https://mybatis.plus/guide/mybatisx-idea-plugin.html#%E5%8A%9F%E8%83%BD)
# 性能分析插件，spy，生产环境可以去除

# 加入拦截器，字段加密解密
* 创建拦截器 EncryptFieldInterceptor,对需要加密的字段进行加密，拦截insert和update

```text
@Intercepts({
        // type 指定代理对象，method 指定代理方法，args 指定type代理类中method方法的参数
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),

})
@Slf4j
public class EncryptFieldInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        Object parameter = null;
        if (invocation.getArgs().length>1){
            parameter=invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        log.info("==> intercept  SQL_ID: [{}]",sqlId);
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnVal = null;
        String sql = genSql(configuration,boundSql);
        log.info("==> intercept  SQL: [{}]",sql);
        returnVal = invocation.proceed();
        return returnVal;
    }

    private String genSql(Configuration configuration, BoundSql boundSql) {

        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size()>0 && null != parameterObject){
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
                sql = sql.replaceFirst("\\?",getParameterVal(parameterObject));
            }else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)){
                        Object value = metaObject.getValue(propertyName);

                        if (EncryptField.FULL_NAME.equalsIgnoreCase(propertyName)||EncryptField.PASSWORD.equalsIgnoreCase(propertyName)){
                            log.info("==> genSql before [{}]: [{}]",propertyName,value);
                            value = AesUtils.AESEncode(EncryptField.AES_KEY,value.toString());
                            log.info("==> genSql after [{}]: [{}]",propertyName,value);
                            metaObject.setValue(propertyName,value);

                        }

                        sql = sql.replaceFirst("\\?",getParameterVal(value));
                    }else if(boundSql.hasAdditionalParameter(propertyName)){
                        Object value = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",getParameterVal(value));
                    }

                }
            }
        }
        return sql;
    }

    private String getParameterVal(Object obj) {
        String val ;
        if (obj instanceof String){
            val = "'"+obj.toString()+"'";
        }else if(obj instanceof Date){
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT, Locale.CHINA);
            val = "'"+dateFormat.format(obj)+"'";
        }else {
            if (null != obj){
                val = obj.toString();
            }else {
                val = "";
            }
        }
        return val;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
```
* 创建解密拦截器，对select的数据库结果加密字段进行解密展示

```text
@Intercepts({
        // type 指定代理对象，method 指定代理方法，args 指定type代理类中method方法的参数
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class}),

})
@Slf4j
public class DecryptFieldInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.info("==> intercept  SQL_ID: [{}]",sqlId);
        Object proceed = invocation.proceed();
        if (sqlId.contains("selectCount")){
            log.warn("==> SQL 语句类型是[selectCount] 直接返回结果");
            return proceed;
        }
        Object parameter = null;
        if (invocation.getArgs().length>1){
            parameter=invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = genSql(configuration,boundSql);
        if (sql.contains("count")||sql.contains("COUNT")||sql.contains("Count")){
            log.warn("==> SQL 语句包含[count|COUNT|Count] 直接返回结果");
            return proceed;
        }
        String result = JSONObject.toJSONString(proceed);
        log.info("==> SQL 查询结果 [{}]",result);
        JSONArray array = JSONArray.parseArray(result);
        log.info("==> SQL 查询结果转换json [{}]",array);

        JSONArray retArray = new JSONArray();
        for (Object o : array) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
            jsonObject.put(EncryptField.PASSWORD, AesUtils.AESDecode(EncryptField.AES_KEY,jsonObject.getString(EncryptField.PASSWORD)));
            // 这种的最好还是统一
            if (jsonObject.containsKey(EncryptField.FULL_NAME)){
                jsonObject.put(EncryptField.FULL_NAME, AesUtils.AESDecode(EncryptField.AES_KEY,jsonObject.getString(EncryptField.FULL_NAME)));

            }else{
                jsonObject.put("full_name", AesUtils.AESDecode(EncryptField.AES_KEY,jsonObject.getString("full_name")));

            }
            retArray.add(jsonObject);
        }
        return retArray;
    }

    private String genSql(Configuration configuration, BoundSql boundSql) {

        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size()>0 && null != parameterObject){
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
                sql = sql.replaceFirst("\\?",getParameterVal(parameterObject));
            }else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)){
                        Object value = metaObject.getValue(propertyName);

                        if (EncryptField.FULL_NAME.equalsIgnoreCase(propertyName)||EncryptField.PASSWORD.equalsIgnoreCase(propertyName)){
                            log.info("==> genSql before [{}]: [{}]",propertyName,value);
                            value = AesUtils.AESEncode(EncryptField.AES_KEY,value.toString());
                            log.info("==> genSql after [{}]: [{}]",propertyName,value);
                            metaObject.setValue(propertyName,value);

                        }

                        sql = sql.replaceFirst("\\?",getParameterVal(value));
                    }else if(boundSql.hasAdditionalParameter(propertyName)){
                        Object value = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",getParameterVal(value));
                    }

                }
            }
        }
        return sql;
    }

    private String getParameterVal(Object obj) {
        String val ;
        if (obj instanceof String){
            val = "'"+obj.toString()+"'";
        }else if(obj instanceof Date){
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT, Locale.CHINA);
            val = "'"+dateFormat.format(obj)+"'";
        }else {
            if (null != obj){
                val = obj.toString();
            }else {
                val = "";
            }
        }
        return val;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
```
* 配置拦截器使拦截器生效
```text
@SpringBootConfiguration
public class EncryptionMapperConfig {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public String encryptionMapperConfig(){
        EncryptFieldInterceptor encryptFieldInterceptor = new EncryptFieldInterceptor();
        DecryptFieldInterceptor decryptFieldInterceptor = new DecryptFieldInterceptor();
        Properties properties = new Properties();
        properties.setProperty("username","zuiyu");
        encryptFieldInterceptor.setProperties(properties);
        sqlSessionFactory.getConfiguration().addInterceptor(encryptFieldInterceptor);
        sqlSessionFactory.getConfiguration().addInterceptor(decryptFieldInterceptor);
        return "encryptionMapperConfig";
    }
}
```

## 企业版加密
参考
[mybatis-mate](https://gitee.com/baomidou/mybatis-mate-examples/tree/master/mybatis-mate-encrypt)

package com.tz.mybatisplus.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tz.mybatisplus.common.encrypt.EncryptField;
import com.tz.mybatisplus.common.util.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName DecryptFieldInterceptor
 * @Description mapper 拦截器，处理sql语句，解密数据
 * @Date 09:56 2022/3/30
 **/
@Intercepts({
        // type 指定代理对象，method 指定代理方法，args 指定type代理类中method方法的参数
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),

})
@Slf4j
@Component
public class DecryptFieldInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.info("==> intercept  SQL_ID: [{}]", sqlId);
        Object proceed = invocation.proceed();
        if (sqlId.contains("selectCount")) {
            log.warn("==> SQL 语句类型是[selectCount] 直接返回结果");
            return proceed;
        }
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = genSql(configuration, boundSql);
        if (sql.contains("count") || sql.contains("COUNT") || sql.contains("Count")) {
            log.warn("==> SQL 语句包含[count|COUNT|Count] 直接返回结果");
            return proceed;
        }
        String result = JSONObject.toJSONString(proceed);
        log.info("==> SQL 查询结果 [{}]", result);
        JSONArray array = JSONArray.parseArray(result);
        log.info("==> SQL 查询结果转换json [{}]", array);

        JSONArray retArray = new JSONArray();
        for (Object o : array) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
            jsonObject.put(EncryptField.PASSWORD, AesUtils.AESDecode(EncryptField.AES_KEY, jsonObject.getString(EncryptField.PASSWORD)));
            // 这种的最好还是统一
            if (jsonObject.containsKey(EncryptField.FULL_NAME)) {
                jsonObject.put(EncryptField.FULL_NAME, AesUtils.AESDecode(EncryptField.AES_KEY, jsonObject.getString(EncryptField.FULL_NAME)));

            } else {
                jsonObject.put("full_name", AesUtils.AESDecode(EncryptField.AES_KEY, jsonObject.getString("full_name")));

            }
            retArray.add(jsonObject);
        }
        return retArray;
    }

    private String genSql(Configuration configuration, BoundSql boundSql) {

        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && null != parameterObject) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterVal(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object value = metaObject.getValue(propertyName);

                        if (EncryptField.FULL_NAME.equalsIgnoreCase(propertyName) || EncryptField.PASSWORD.equalsIgnoreCase(propertyName)) {
                            log.info("==> genSql before [{}]: [{}]", propertyName, value);
                            value = AesUtils.AESEncode(EncryptField.AES_KEY, value.toString());
                            log.info("==> genSql after [{}]: [{}]", propertyName, value);
                            metaObject.setValue(propertyName, value);

                        }

                        sql = sql.replaceFirst("\\?", getParameterVal(value));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object value = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterVal(value));
                    }

                }
            }
        }
        return sql;
    }

    private String getParameterVal(Object obj) {
        String val;
        if (obj instanceof String) {
            val = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            val = "'" + dateFormat.format(obj) + "'";
        } else {
            if (null != obj) {
                val = obj.toString();
            } else {
                val = "";
            }
        }
        return val;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

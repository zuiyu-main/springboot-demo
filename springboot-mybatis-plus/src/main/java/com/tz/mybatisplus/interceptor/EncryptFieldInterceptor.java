package com.tz.mybatisplus.interceptor;

import com.tz.mybatisplus.common.encrypt.EncryptField;
import com.tz.mybatisplus.common.util.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
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
 * @ClassName EncryptFieldInterceptor
 * @Description mapper 拦截器，处理sql语句,加密字段存储
 * @Date 09:56 2022/3/30
 **/
@Intercepts({
        // type 指定代理对象，method 指定代理方法，args 指定type代理类中method方法的参数
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),

})
@Slf4j
@Component
public class EncryptFieldInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        log.info("==> intercept  SQL_ID: [{}]", sqlId);
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnVal = null;
        String sql = genSql(configuration, boundSql);
        log.info("==> intercept  SQL: [{}]", sql);
        returnVal = invocation.proceed();
        return returnVal;
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

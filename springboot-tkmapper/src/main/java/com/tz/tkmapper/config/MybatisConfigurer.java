package com.tz.tkmapper.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author liBai
 * @Classname MybatisConfigurer
 * @Description TODO
 * @Date 2019-06-02 10:42
 */
@SpringBootConfiguration
public class MybatisConfigurer {
    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.tz.tkmapper.bean");
        return bean.getObject();
    }

}

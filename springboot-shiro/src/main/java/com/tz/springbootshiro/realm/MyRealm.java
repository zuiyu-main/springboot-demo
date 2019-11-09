package com.tz.springbootshiro.realm;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author tz
 * @Classname MyRealm
 * @Description
 * @Date 2019-11-09 11:44
 */
@Component
public class MyRealm {
    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("tz=test123,user\n" +
                "jill.coder=password,admin");

        realm.setRoleDefinitions("admin=read,write\n" +
                "user=read");
        realm.setCachingEnabled(true);
        return realm;
    }
}

package com.tz.serurity.simple.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * @author tz
 * @Classname MyUserDetailService
 * @Description
 * @Date 2019-12-18 11:44
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        UserDetails userDetails = User.withUsername("tz").password("$2a$10$pTffBwh9mawjeGG9K5ZhbenBfWQRV1aFVgZqVt59eM67iJhDqARyG").authorities("p1").build();
//        return userDetails;
        // 模拟一个用户，替代数据库获取逻辑
        MyUser user = new MyUser();
        user.setUserName(s);
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        // 输出加密后的密码
        System.out.println(user.getPassword());

        return new User(s, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("p1,p2"));

    }

}

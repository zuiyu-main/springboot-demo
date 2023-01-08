package com.tz.activiti.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName DemoSecurityConfiguration
 * @Description
 * @Date 17:56 2021/6/20
 **/
@SpringBootConfiguration
public class DemoSecurityConfiguration {
    private Logger logger = LoggerFactory.getLogger(DemoSecurityConfiguration.class);

    @Bean
    public UserDetailsService myUserDetailsService() {

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        String[][] usersGroupsAndRoles = {
                {"user1", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"user2", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"user3", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"user4", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"user5", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"user6", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"user7", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"system", "password", "ROLE_ACTIVITI_USER"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };

        for (String[] user : usersGroupsAndRoles) {
            List<String> authoritiesStrings = asList(Arrays.copyOfRange(user, 2, user.length));
            logger.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
        }

        return inMemoryUserDetailsManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

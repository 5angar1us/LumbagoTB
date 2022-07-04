package com.example.TradeBoot.configs;

import com.example.TradeBoot.ui.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
////    @Autowired
////    private DataSource dataSource;
//
//    private final PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeRequests()
//                .antMatchers("/").permitAll();
//        http.csrf().disable();
//
////        http
////                .authorizeRequests()
////                    .antMatchers("/").permitAll()
////                    .anyRequest().authenticated()
////                .and()
////                    .formLogin()
////                    .loginPage("/login")
////                    .permitAll()
////                .and()
////                    .logout()
////                    .permitAll();
//    }
//
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.jdbcAuthentication()
////                .dataSource(dataSource)
////                .passwordEncoder(getPasswordEncoder())
////                .usersByUsernameQuery("select username, password, active from usr where username=?")
////                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
////
////    }
//
//
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("Admin")
//                        .password("lA6Um9JAUTiFbXUe50")
//                        .roles(Role.ADMIN.toString())
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//
//
//
//    @Value("${spring.security.debug:false}")
//    boolean securityDebug;
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.debug(securityDebug);
//    }
//
////    @Bean
////    public PasswordEncoder getPasswordEncoder2(){
////        return passwordEncoder;
////    }
//}

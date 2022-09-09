package com.example.TradeBoot.configs;

import com.example.TradeBoot.ui.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    static final Logger log =
            LoggerFactory.getLogger(SecurityConfiguration.class);


    @Value("${app.security:true}")
    private Boolean isShowLoginPage;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            if(isShowLoginPage == false) {
                http
                        .requiresChannel().anyRequest().requiresSecure()
                        .and()
                        .authorizeRequests()
                        .antMatchers("/").permitAll();
                http.csrf().disable();
            }
            else if(isShowLoginPage){
                    http
                            .requiresChannel().anyRequest().requiresSecure()
                            .and()
                            .authorizeRequests()
                            .antMatchers("/static/js/**", "/static/css/**").permitAll()
                            .and()
                            .authorizeRequests()
                            .anyRequest().authenticated()
                            .and()
                            .formLogin()
                            .loginPage("/login")
                            .permitAll()
                            .and()
                            .logout()
                            .permitAll();
                }
        log.info("Show login page: " + isShowLoginPage);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("Admin")
                .password("lA6Um9JAUTiFbXUe50")
                .roles(Role.ADMIN.toString())
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

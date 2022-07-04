package com.example.TradeBoot.configs;

import com.example.TradeBoot.TradeBootApplication;
import com.example.TradeBoot.ui.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    static final Logger log =
            LoggerFactory.getLogger(SecurityConfiguration.class);


    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        switch (activeProfile){
            case "dev" ->{
                http
                        .authorizeRequests()
                        .antMatchers("/").permitAll();
                http.csrf().disable();
                log.debug("Active endpoints dev");
            }
            case "production" ->{
                http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
                log.debug("Active endpoints production");
            }
            default -> throw new IllegalArgumentException("activeProfile");
        }
        log.info("Security profile" + activeProfile);
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

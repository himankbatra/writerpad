package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.services.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http
           // .csrf().ignoringAntMatchers("/api/**","/h2-console/**")
            .csrf().ignoringAntMatchers("/api/**")
            .and()
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/profiles/{username}").permitAll()
            //  .antMatchers("/h2-console/**").permitAll()
           //     .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyRequest().authenticated()
      //    .and().headers().frameOptions().disable()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
            .and()
                    .logout()
                    .permitAll()
            .and()
              .httpBasic();

    }
    // @formatter:on

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_WRITER\n" +
                "ROLE_ADMIN > ROLE_EDITOR");
        return roleHierarchy;
    }

}



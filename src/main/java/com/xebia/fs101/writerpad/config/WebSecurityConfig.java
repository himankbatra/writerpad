package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.services.security.CustomUserDetailsService;
import com.xebia.fs101.writerpad.services.security.jwt.JwtSecurityConfigurer;
import com.xebia.fs101.writerpad.services.security.jwt.JwtTokenSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenSuccessHandler jwtTokenSuccessHandler;

    @Autowired
    private JwtSecurityConfigurer jwtSecurityConfigurer;

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // .csrf().ignoringAntMatchers("/api/**","/h2-console/**")
                .csrf()
                .disable()
                /*   .ignoringAntMatchers("/api/**")
                   .and()*/
                .authorizeRequests()
                //.antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/profiles/{username}").permitAll()
                //  .antMatchers("/h2-console/**/**").permitAll()
                //  .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(this.jwtTokenSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .apply(jwtSecurityConfigurer)
                .and()
                .headers()
                .cacheControl();
      /*  .frameOptions().sameOrigin()
        .httpBasic();*/


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
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_WRITER\n"
                + "ROLE_ADMIN > ROLE_EDITOR");
        return roleHierarchy;
    }

}



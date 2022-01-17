package com.decagon.fitnessoapp.security;

import com.decagon.fitnessoapp.model.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PersonDetailsService personDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(personDetailsService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll()
                .antMatchers("/signUp").permitAll()
                .antMatchers("/login").hasAnyRole(Role.PREMIUM.toString(),Role.ADMIN.toString())


                .antMatchers("/").permitAll()
                .and()
                .formLogin();
    }
}

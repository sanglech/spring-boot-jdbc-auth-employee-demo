package com.christian.springbootthymeleafdemo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("appDataSource")
    private DataSource appDataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // use jdbc authentication ... oh yeah!!!
        auth.jdbcAuthentication().dataSource(appDataSource);

    }
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/employees/list").hasAnyRole("MANAGER","ADMIN","EMPLOYEE")
                .antMatchers("/employees/showFormForAdd").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/showSignUpPage").permitAll()
                .and()
                .formLogin()
                    .loginPage("/showMyLoginPage")
                    .loginProcessingUrl("/authenticateTheUser")
                    .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");

                /*
        http.authorizeRequests()
                .antMatchers("/").anonymous()
                .antMatchers("/employees/list").anonymous()
                .antMatchers("/employees/showFormForAdd").anonymous()
                .antMatchers("/employees/showFormForUpdate").anonymous()
                .antMatchers("/employees/save").anonymous()
                .antMatchers("/employees/delete").anonymous()
                .anyRequest().authenticated();*/

    }
}

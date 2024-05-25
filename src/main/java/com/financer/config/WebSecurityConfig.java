/*
 * Adriel Swisher
 * CST 452
 * 
 * Web security configuration for application and user authentication
 */
package com.financer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.financer.persistence.data.UserAuthDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Autowired
        UserDetailsService userAuthService;

        @Autowired
        BCryptPasswordEncoder encoder;

        @Bean
        protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
                http.userDetailsService(userAuthService());
                http.authorizeHttpRequests(requests -> requests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .antMatchers("/assets/**").permitAll()
                                .antMatchers("/data/manage***").hasAuthority("ROLE_ADMIN")
                                .antMatchers("/users/manage***").hasAuthority("ROLE_ADMIN")
                                .anyRequest()
                                .authenticated()
                                )
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/accessDenied"))
                                .formLogin(login -> login
                                                .loginPage("/login").permitAll()
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .failureUrl("/denied")
                                                .defaultSuccessUrl("/reports/manageReports")
                                )
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .invalidateHttpSession(true)
                                                .logoutSuccessUrl("/login"));

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return new UserAuthDetailsService();
        }

        private UserDetailsService userAuthService() {
                return userAuthService;
        }
}

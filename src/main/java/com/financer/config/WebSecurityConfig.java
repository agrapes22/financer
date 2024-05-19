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
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.financer.persistence.data.UserAuthService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userAuthService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(userAuthService());
        http.authenticationProvider(authProvider())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .antMatchers("/assets/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .failureUrl("/denied"))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login"));

		return http.build();
	}

	private UserDetailsService userAuthService() {
        return userAuthService;
    }
}

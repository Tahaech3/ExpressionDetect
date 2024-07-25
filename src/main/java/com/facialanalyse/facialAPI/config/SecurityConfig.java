package com.facialanalyse.facialAPI.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        //InMemoryUserDetailsManager
        UserDetails admin = User.withUsername("admin").password(encoder.encode("123")).roles("ADMIN","USER").build();

        UserDetails user = User.withUsername("user").password(encoder.encode("123")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin,user);
    }


    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/welcome").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated()
                .and()
                .authorizeHttpRequests().requestMatchers("/auth/admin/**").authenticated()
                .requestMatchers("/api/products/**").hasRole("USER")
                .requestMatchers("/api/expressions/**").hasRole("USER")
                .requestMatchers("/api/recommendations/**").hasRole("USER")
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .and().formLogin()
                .and().build();
    }


    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("ROLE_USER");
        return authorityMapper;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
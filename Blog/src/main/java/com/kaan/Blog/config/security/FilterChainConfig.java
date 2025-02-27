package com.kaan.Blog.config.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class FilterChainConfig {

    private JwtAuthFilter jwtAuthFilter;

    private AuthenticationProvider authenticationProvider;

    private AuthenticationManager authenticationManager;

    public FilterChainConfig(JwtAuthFilter jwtAuthFilter, @Lazy AuthenticationProvider authenticationProvider, @Lazy AuthenticationManager authenticationManager) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement((httpSecuritySessionManagementConfigurer) -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((httpSecurityCorsConfigurer) -> httpSecurityCorsConfigurer.configurationSource(corsFilter()))
                .authenticationProvider(authenticationProvider)
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                                .anyRequest().permitAll())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/"))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.clearAuthentication(true)
                        .deleteCookies("Authorization"));
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // İzin verilen origin
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS gibi tüm metodlara izin ver
        configuration.addAllowedHeader("*"); // Tüm header'lara izin ver
        configuration.setAllowCredentials(true); // Credentials (örneğin JWT) için true olmalı

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Tüm endpoint'ler için geçerli
        return source;
    }
}

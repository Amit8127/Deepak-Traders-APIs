package com.deepakTraders.generalstore.config;

import com.deepakTraders.generalstore.config.JWTSecurity.JwtConstant;
import com.deepakTraders.generalstore.config.JWTSecurity.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

//        // Disable CSRF, set up URL-based authorization, handle exceptions, and configure session management
//        httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors
//                        .configurationSource(request -> {
//                            CorsConfiguration corsConfiguration = new CorsConfiguration();
//                            corsConfiguration.setAllowCredentials(true);
//                            corsConfiguration.addAllowedOriginPattern("*"); // * for testing and local
//                            corsConfiguration.addAllowedHeader("Authorization");
//                            corsConfiguration.addAllowedHeader("Content-Type");
//                            corsConfiguration.addAllowedHeader("Accept");
//                            corsConfiguration.addAllowedMethod("*");
//                            corsConfiguration.setMaxAge(3600L);
//                            return corsConfiguration;
//                        }))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/**").authenticated().anyRequest().permitAll())
////                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // Add JWT Authentication Filter before UsernamePasswordAuthenticationFilter
////        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class).csrf().disable()
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration corsConfiguration = new CorsConfiguration();

                        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // frontend URLs to access this backend
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setExposedHeaders(Arrays.asList(JwtConstant.JWT_HEADER));
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                })
                .and().httpBasic().and().formLogin();
                return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

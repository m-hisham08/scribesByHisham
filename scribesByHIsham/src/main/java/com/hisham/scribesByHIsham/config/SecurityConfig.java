package com.hisham.scribesByHIsham.config;

import com.hisham.scribesByHIsham.utils.JWTAuthenticatiionFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    @Bean
    public JWTAuthenticatiionFilter jwtAuthenticationFilter(){
        return new JWTAuthenticatiionFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET,"/api/articles", "/api/articles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/**", "/api/users/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/articles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/articles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/articles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/articles").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/upload").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/like/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/articles/*/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/articles/*/comments").hasRole("USER")
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}

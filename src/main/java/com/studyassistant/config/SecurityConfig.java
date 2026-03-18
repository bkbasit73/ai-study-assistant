package com.studyassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/register", "/login", "/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            )

            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/h2-console/**")
            )

            .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
            )

            .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutUrl("/logout")                // logout endpoint
                    .logoutSuccessUrl("/login?logout")   // redirect after logout
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
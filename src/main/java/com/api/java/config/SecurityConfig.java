package com.api.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authz -> authz
                         .requestMatchers("/user/createUser").permitAll()//Declara que esa direccion no necesita autentificacion(es decir loguearse)
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .successHandler(successHandler())
                        .permitAll()
                )

                .sessionManagement( sesion ->
                    sesion
                            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                            .invalidSessionUrl("/login")
                            .sessionFixation()
                            .migrateSession()
                            .maximumSessions(1)
                            .expiredUrl("/login")
                            .sessionRegistry(sessionRegistry())

                )
                .csrf(HttpSecurity-> HttpSecurity.disable())


                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect("/user");
        });
    }
   /* @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }*/
}


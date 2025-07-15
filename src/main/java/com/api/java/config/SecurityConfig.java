package com.api.java.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**Configura la seguridad de la aplicación, habilitando autenticación JWT y protegiendo las rutas.*/

@Configuration      // Indica que es una clase de configuración de Spring
@EnableWebSecurity      // Activa la seguridad web en la aplicación
@EnableMethodSecurity   // Habilita seguridad a nivel de metodo (como @PreAuthorize)
@RequiredArgsConstructor    // Genera constructor con los campos final automáticamente

public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;     // Proveedor de autenticación, puede ser personalizado para validar usuarios
    private final JwtAuthenticationFilter jwtAuthenticationFilter;   // Filtro personalizado que intercepta las requests y valida el JWT

    /** Metodo principal que define la cadena de filtros de seguridad:
     * - Desactiva protección CSRF (no necesaria para APIs REST con JWT)
     * - Usa sesiones sin estado: no se guarda sesión en el servidor
     * - Permite acceso sin autenticación a /auth/**
     * - Protege rutas /admin/** y /user/** según roles
     * - Agrega el filtro de JWT antes del filtro de autenticación estándar */

    @Bean // Spring gestiona esta instancia como un bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf  // Desactiva CSRF ya que JWT no usa cookies ni sesiones
                        .disable()
                )
                .sessionManagement(session -> session   // Configura la gestión de sesiones como sin estado
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authRequest -> authRequest   // Configura las reglas de autorización por rutas
                        .requestMatchers("/auth/**").permitAll()     // Permite acceso libre a rutas de autenticación
                        // Requiere rol ADMIN para acceder a /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Permite acceso a /user/** tanto a USER como ADMIN
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // Cualquier otra request debe estar autenticada
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)     // Define el proveedor de autenticación personalizado
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   // Inserta el filtro JWT antes del filtro estándar de autenticación
                .build();   // Construye y devuelve el SecurityFilterChain final
    }
}
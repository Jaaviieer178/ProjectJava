package com.api.java.config;
import com.api.java.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Clase de configuración de seguridad que define los componentes (beans)
 * necesarios para la autenticación de usuarios dentro de Spring Security. */

@Configuration // Indica que esta clase define beans para el contenedor de Spring
@RequiredArgsConstructor // Genera constructor para inyectar automáticamente los campos final
public class ApplicationConfig {
    private final IUserRepository userRepository;       // Repositorio JPA que accede a los usuarios desde la base de datos

    /** Bean que provee el AuthenticationManager, utilizado para validar credenciales manualmente,
     * como en el flujo de login programático. Spring Security lo construye internamente.
     * @param config configuración de seguridad proporcionada por Spring
     * @return AuthenticationManager que se usa para autenticar mediante username/password
     * @throws Exception si ocurre un error al construir el manager */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** Bean que define el proveedor de autenticación.
     * Usa DaoAuthenticationProvider para comparar credenciales con los datos de la base
     * usando UserDetailsService y codifica las contraseñas con BCrypt.
     *  Aunque algunas APIs están marcadas como deprecated en Spring Security 6,
     * esta configuración sigue funcionando correctamente.
     * @return AuthenticationProvider para validar credenciales */

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());   // Servicio que carga los datos del usuario desde la base
        provider.setPasswordEncoder(passwordEncoder());     // Codificador de contraseñas que aplica hashing seguro
        return provider;
    }

    /** Bean que define el mecanismo de encriptación de contraseñas.
     * BCrypt es recomendado por Spring Security para su resistencia a ataques de fuerza bruta.
     * @return PasswordEncoder basado en BCrypt */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Bean que implementa UserDetailsService, utilizado para cargar los datos de usuario
     * durante el proceso de autenticación. Utiliza el repositorio JPA para buscar por username.
     * @return implementación de UserDetailsService que recupera usuarios desde base de datosn */

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}

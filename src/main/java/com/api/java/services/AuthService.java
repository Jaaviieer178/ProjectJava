package com.api.java.services;
import com.api.java.dto.AuthResponse;
import com.api.java.dto.LoginRequest;
import com.api.java.dto.RegisterRequest;
import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import com.api.java.models.UserModel;
import com.api.java.repositories.IRoleRepository;
import com.api.java.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

/** Servicio encargado de gestionar la autenticación y el registro de usuarios.
 * Genera tokens JWT, aplica codificación de contraseñas y asigna roles por defecto. */

@Service // Define esta clase como un componente de servicio para Spring
@RequiredArgsConstructor // Genera automáticamente el constructor con los campos final
public class AuthService {
    private final IUserRepository userRepository;   // Repositorio para acceder a los datos de usuarios
    private final JwtService jwtService;     // Servicio para generar, validar y extraer datos desde tokens JWT
    private final IRoleRepository roleRepository;       // Repositorio para acceder a roles (como ROLE_USER, ROLE_ADMIN, etc.)
    private final PasswordEncoder passwordEncoder;      // Bean que codifica contraseñas de forma segura usando BCrypt
    private final AuthenticationManager authenticationManager;      // Bean que gestiona la autenticación por username y contraseña

    /** Autentica al usuario con sus credenciales y genera un token JWT si son válidas.
     * @param request objeto con username y contraseña enviados por el cliente
     * @return objeto AuthResponse con el token JWT generado */

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));      // Realiza autenticación con username y password usando el AuthenticationManager

        UserDetails user = userRepository.findByUsername(request.getUsername())     // Busca el usuario en la base de datos (ya autenticado)
                .orElseThrow(); // Lanza excepción si no existe

        String token = jwtService.getToken(user);       // Genera el token JWT para el usuario

        return AuthResponse.builder()       // Retorna la respuesta con el token
                .token(token)
                .build();
    }

    /** Registra un nuevo usuario con rol USER por defecto y genera un token JWT.
     * @param request objeto con los datos personales del nuevo usuario
     * @return objeto AuthResponse con el token JWT generado */

    public AuthResponse register(RegisterRequest request) {
        RoleModel userRole = roleRepository.findByName(ERole.USER)      // Busca el rol USER en la base de datos
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        Set<RoleModel> roles = new HashSet<>();         // Crea el conjunto de roles asignados (solo USER)
        roles.add(userRole);

        UserModel user = UserModel.builder()        // Construye el objeto UserModel con los datos del formulario
                .dni(request.getDni())
                .username(request.getUsername())
                .lastname(request.getLastname())
                .firstname(request.getFirstname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))         // Codifica la contraseña
                .country(request.getCountry())
                .role(roles)         // Asigna el rol USER
                .build();
        userRepository.save(user);      // Persiste el usuario en la base de datos

        return AuthResponse.builder()       // Genera el token JWT para el nuevo usuario
                .token(jwtService.getToken(user))
                .build();
    }
}

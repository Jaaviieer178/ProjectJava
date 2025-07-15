package com.api.java.config;
import com.api.java.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/** Filtro personalizado que se ejecuta una vez por request (OncePerRequestFilter).
 * Su propósito es interceptar cada petición HTTP, extraer el token JWT si existe,
 * validar el token y configurar la autenticación en el contexto de seguridad.*/

@Component
@RequiredArgsConstructor // Genera constructor con los atributos final
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;    // Servicio que gestiona operaciones con tokens JWT (validación, extracción de claims, etc.)


    /** Metodo principal del filtro.
     * Se ejecuta en cada petición y define la lógica para extraer el token y configurar la autenticación. */

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);          // Extrae el token JWT del header Authorization
        final String username;
        if (token == null) {        // Si no hay token en la request, continúa sin modificar la autenticación
            filterChain.doFilter(request, response);
            return;
        }
        username = jwtService.getUsernameFromToken(token);      // Extrae el nombre de usuario del token (generalmente el email o username)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {   // Si hay un username válido y aún no se ha autenticado esta sesión
            Claims claims = jwtService.getAllClaims(token);     // Obtiene todos los claims (datos embebidos en el token)
            @SuppressWarnings("unchecked") //  Suprime el warning del compilador
            List<String> roles = claims.get("roles", List.class);   // Extrae la lista de roles desde los claims del token
            List<GrantedAuthority> authorities = roles.stream()     // Convierte los roles en autoridades que Spring Security entiende
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authToken =         // Construye el token de autenticación con el usuario y sus roles
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));   // Agrega detalles adicionales como IP, agente, etc. desde la request
            SecurityContextHolder.getContext().setAuthentication(authToken);    // Guarda el token de autenticación en el contexto de seguridad
        }
        filterChain.doFilter(request, response);    // Continúa con la cadena de filtros
    }

    /** Extrae el token JWT desde el encabezado Authorization de la request.
     * Solo lo devuelve si el encabezado empieza con "Bearer".*/

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
            return authHeader.substring(7); //  Elimina el prefijo "Bearer "
        }
        return null;
    }
}
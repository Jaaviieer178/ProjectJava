package com.api.java.services;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/** Servicio encargado de generar, validar y extraer información de tokens JWT.
 * Utiliza el algoritmo HMAC SHA-256 (HS256) y una clave secreta codificada en Base64. */

@Service // Marca la clase como componente de servicio para que Spring la maneje como un bean
@RequiredArgsConstructor // Genera automáticamente el constructor para las dependencias 'final'
public class JwtService {
    /** Clave secreta utilizada para firmar y verificar el JWT.
     * Codificada en Base64 para que cumpla con el requisito de tamaño mínimo (256 bits). */

    private static final String SECRET_KEY = "Zm9ydGFsZG9zX2RlYmVuX2V4cG9ydGFyX2VzdGFfY2xhdmU=";

    /** Genera un token JWT para un usuario autenticado.
     * Extrae los roles del usuario desde sus GrantedAuthority y los guarda como claim 'roles'.
     * @param user objeto UserDetails que representa al usuario autenticado
     * @return string del token JWT generado */

    public String getToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream()  // Extrae los roles del usuario como lista de strings (e.g., ["ROLE_ADMIN", "ROLE_USER"])
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));


        return getToken(claims, user);  // Genera el token con claims personalizados
    }

    /** MEtodo privado que crea un JWT con claims personalizados.
     * @param extraClaims mapa con claims adicionales (roles, etc.)
     * @param user        objeto UserDetails para extraer nombre de usuario
     * @return token JWT generado */

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .claims(extraClaims)     // Agrega claims personalizados al payload
                .subject(user.getUsername())    // Establece el subject como el username
                .issuedAt(new Date(System.currentTimeMillis()))     // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))     // Expira en 24 horas
                .signWith(getKey())  // Firma con clave secreta usando HS256
                .compact();     // Compila el token JWT final
    }

    /** Genera una clave HMAC válida para firmar y verificar tokens usando la clave secreta.
     * @return objeto SecretKey compatible con el algoritmo HS256 */

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica Base64 a bytes
        return Keys.hmacShaKeyFor(keyBytes); // Genera clave HMAC SHA-256 con esos bytes
    }

    /** Extrae el nombre de usuario (subject) desde un token JWT.
     * @param token token JWT firmado
     * @return nombre de usuario embebido en el token */

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject); // Usa Claims::getSubject como extractor
    }

    /** Valida si un token es legítimo y pertenece al usuario esperado.
     * @param token        el JWT a validar
     * @param userDetails  datos del usuario que se espera (usado en login)
     * @return true si el token es válido, false si no */

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /** Devuelve todos los claims embebidos en el token firmado.
     * Usa el nuevo API de JWT 0.12+ con JwtParserBuilder.
     * @param token JWT firmado
     * @return objeto Claims con el payload del token */

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()              // Obtiene un JwtParserBuilder
                .verifyWith(getKey())          // Define la clave para verificar la firma
                .build()              // Compila el parser final
                .parseSignedClaims(token)      // Parsea los claims firmados
                .getPayload();               // Extrae el payload como Claims
    }

    /** Extrae un claim específico desde el token usando una función extractora.
     * @param token           token JWT
     * @param claimsResolver  función que define qué parte de los claims extraer
     * @param <T>             tipo de dato que se quiere extraer (String, Date, List, etc.)
     * @return dato extraído del claim */

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Obtiene la fecha de expiración del token.
     * @param token JWT firmado
     * @return objeto Date con la fecha de expiración */

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /** Verifica si el token ya expiró comparando la fecha actual con su expiración.
     * @param token JWT firmado
     * @return true si el token expiró, false si sigue vigente */

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
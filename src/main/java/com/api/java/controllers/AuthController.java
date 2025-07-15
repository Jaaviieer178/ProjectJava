package com.api.java.controllers;
import com.api.java.dto.AuthResponse;
import com.api.java.dto.LoginRequest;
import com.api.java.dto.RegisterRequest;
import com.api.java.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controlador REST para autenticación de usuarios.
 * Contiene endpoints públicos para login y registro. */

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones para iniciar sesión y registrar usuarios")
public class AuthController {

    private final AuthService authService;

    /** Inicia sesión con credenciales de usuario.
     * @param request objeto con username y contraseña
     * @return token JWT si las credenciales son válidas */

    @Operation(summary = "Iniciar sesión", description = "Verifica credenciales y devuelve un token JWT si son válidas")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de acceso", required = true) @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /** Registra un nuevo usuario con rol USER por defecto.
     * @param request datos del nuevo usuario
     * @return token JWT si el registro es exitoso */

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario con rol USER y devuelve un token JWT")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
    @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario duplicado")})
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo usuario", required = true) @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}

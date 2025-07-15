package com.api.java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Respuesta de autenticación que contiene el token JWT generado al iniciar sesión exitosamente. */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    /** Token de acceso generado para el usuario autenticado. */
    @Schema(description = "Token JWT generado tras autenticación exitosa", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}

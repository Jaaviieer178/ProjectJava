package com.api.java.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** DTO utilizado para iniciar sesión en el sistema.
 * Contiene las credenciales mínimas requeridas para autenticación. */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /** Nombre de usuario con el que se autentica. */
    @Schema(description = "Nombre de usuario", example = "juanperez")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    /** Contraseña correspondiente al usuario. */
    @Schema(description = "Contraseña del usuario", example = "1234Secure!")
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}

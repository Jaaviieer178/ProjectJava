package com.api.java.dto;
import com.api.java.models.RoleModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

/** DTO utilizado para registrar un nuevo usuario en el sistema.
 * Contiene los datos básicos requeridos durante el registro inicial.*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /** Número de DNI, debe ser único y no nulo.   */
    @Column(unique = true, nullable = false, name = "dni")
    @NotNull(message = "El DNI es obligatorio")
    @Digits(integer = 8, fraction = 0, message = "El DNI debe contener hasta 8 dígitos sin decimales")
    private Integer dni;

    /** Nombre de usuario deseado (alias o nickname). */
    @Schema(description = "Alias o nombre de usuario", example = "juanperez")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    /** Apellido del usuario. */
    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastname;

    /** Nombre del usuario. */
    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstname;

    /** Correo electrónico válido y único del usuario. */
    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Schema(description = "Contraseña del usuario")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    /** País del usuario. */
    @Schema(description = "País de residencia del usuario", example = "Argentina")
    @NotBlank(message = "El país no puede estar vacío")
    private String country;

    private Set<RoleModel> role;
}

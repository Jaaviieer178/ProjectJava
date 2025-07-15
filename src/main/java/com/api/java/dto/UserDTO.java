package com.api.java.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @Schema(description = "ID único del usuario", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Número de documento nacional de identidad", example = "33666999")
    @NotNull(message = "El DNI es obligatorio")
    @Digits(integer = 8, fraction = 0, message = "El DNI debe ser numérico y tener hasta 8 dígitos")
    private Integer dni;

    @Schema(description = "Nombre de usuario completo", example = "Juanito Pérez")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastname;

    @Schema(description = "Nombre del usuario", example = "Juanito")
    private String firstname;

    @Schema(description = "Correo electrónico del usuario", example = "juaniperez@example.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Schema(description = "Contraseña del usuario")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "País del usuario", example = "Argentina")
    private String country;

    @Schema(description = "Roles asignados al usuario", example = "[\"USER\", \"ADMIN\"]")
    private Set<@NotBlank(message = "Los nombres de los roles no pueden estar vacíos") String> role;
}

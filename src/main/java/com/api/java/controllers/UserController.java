package com.api.java.controllers;
import com.api.java.dto.UserDTO;
import com.api.java.services.UserService;
import  java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Controlador REST para operaciones relacionadas con la entidad Usuario.
 * Expone endpoints para crear, consultar, actualizar y eliminar usuarios.*/

@Tag(name = "Usuario", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Crear un nuevo usuario", description = "Recibe un UserDTO con los datos del nuevo usuario y lo guarda en la base de datos")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los datos del usuario a crear", required = true) @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public List<UserDTO> getUsers() { return userService.getUsers(); }

    @Operation( summary = "Obtener usuario por ID", description = "Busca y devuelve un usuario específico según su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Actualizar un usuario por ID", description = "Modifica los datos de un usuario existente usando su ID y un DTO con nuevos valores")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con los nuevos datos del usuario", required = true) @Valid @RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.updateById(id, user));
    }

    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario específico de la base de datos según su ID")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasAuthority('SUPER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User delete");
    }
}
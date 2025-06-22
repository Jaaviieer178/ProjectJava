package com.api.java.controllers;
import com.api.java.dto.UserDTO;
import com.api.java.services.UserService;
import  java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los usuarios", description = "Retorna una lista con todos los usuarios registrados en el sistema.")
        public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid  @RequestBody UserDTO  user){
        return ResponseEntity.ok(userService.updateById(id, user));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.ok("User delete");
    }

}
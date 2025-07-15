package com.api.java.config;

import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import com.api.java.repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Componente que se ejecuta automáticamente al iniciar la aplicación.
 * Se encarga de inicializar la base de datos con los roles definidos en {@link ERole}
 * evitando duplicados si ya existen.   */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final IRoleRepository roleRepository;   // Repositorio para acceder y persistir entidades RoleModel

    /** Metodo que se ejecuta al arrancar la aplicación Spring.
     *  Si algún rol definido en ERole no existe en la base de datos, lo guarda automáticamente. */
    @Override
    public void run(String... args) {
        for (ERole role : ERole.values()) {  // Recorre todos los valores del enum ERole
            roleRepository.findByName(role)     // Busca si el rol ya existe; si no, lo guarda en la base de datos
                    .orElseGet(() -> roleRepository.save(
                            RoleModel.builder()
                                    .name(role) // Asigna el valor del enum como nombre
                                    .build()
                    ));
        }
    }
}

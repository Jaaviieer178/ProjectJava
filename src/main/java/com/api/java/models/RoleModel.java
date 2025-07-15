package com.api.java.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

/** Entidad que representa los roles del sistema (e.g., ADMIN, USER).
 * Cada instancia corresponde a un registro en la tabla "roles". */

@Data // Genera automáticamente getters, setters, equals, hashCode, toString
@Builder // Permite construir instancias usando patrón builder
@AllArgsConstructor // Genera constructor con todos los atributos
@NoArgsConstructor // Genera constructor sin argumentos
@Entity // Marca la clase como una entidad JPA
@Table(name = "roles") // Define el nombre de la tabla en la base de datos
public class RoleModel {

    /** Identificador único del rol.
     * Se genera automáticamente por la base de datos (auto-incremental). */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del rol (e.g., ROLE_ADMIN, ROLE_USER).
     * Se guarda como string en la base de datos, nunca puede ser null. */

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Guarda el enum como texto legible, no como ordinal
    private ERole name;

    /** Sobrescribe la comparación de objetos basada en el nombre del rol.
     * Dos RoleModel se consideran iguales si tienen el mismo ERole. */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleModel that)) return false;
        return Objects.equals(name, that.name);
    }

    /** Calcula el hash en base al nombre del rol.
     * Esto permite usar RoleModel en estructuras como HashSet o HashMap. */

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

package com.api.java.mapper;
import com.api.java.dto.RoleDTO;
import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;


/** Mapeador de objetos entre {@link RoleModel} y {@link RoleDTO} usando MapStruct.
 * Facilita la conversión entre entidad y DTO, incluyendo actualizaciones parciales
 * y transformaciones auxiliares con representaciones en texto.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    /** Convierte una entidad {@link RoleModel} a su correspondiente {@link RoleDTO}.
     * @param role entidad RoleModel a convertir
     * @return DTO equivalente*/

    RoleDTO RoleModelToDto(RoleModel role);

    /** Convierte un DTO {@link RoleDTO} a su correspondiente {@link RoleModel}.
     * Este metodo puede ser heredado por otras configuraciones.
     * @param roleDTO objeto DTO a convertir
     * @return entidad equivalente */

    @Named("RoleDtoToRole")
    RoleModel RoleDtoToRole(RoleDTO roleDTO);

    /** Actualiza una entidad existente {@link RoleModel} con datos del {@link RoleDTO}.
     * Ignora el campo {@code id} para evitar sobrescribir identificadores.
     * @param dto objeto DTO con nuevos datos
     * @param entity entidad a actualizar */

    @InheritConfiguration(name = "RoleDtoToRole")
    @Mapping(target = "id", ignore = true)
    void updateRoleFromDto(RoleDTO dto, @MappingTarget RoleModel entity);

    /** Convierte una entidad {@link RoleModel} en un {@code String} representativo.
     * Si el rol o su nombre están nulos, devuelve "UNKNOWN".
     * @param role entidad a convertir
     * @return nombre del rol como texto, o "UNKNOWN" si no existe */

    default String toString(RoleModel role) {
        return (role != null && role.getName() != null) ? role.getName().name() : "UNKNOWN";
    }

    /*** Convierte un conjunto de entidades {@link RoleModel} en un conjunto de Strings.
     * Cada rol es transformado con {@link #toString(RoleModel)}.
     * @param roles conjunto de roles a convertir
     * @return conjunto de nombres de roles */

    default Set<String> toStringSet(Set<RoleModel> roles) {
        return roles.stream()
                .map(this::toString)
                .collect(Collectors.toSet());
    }

    /** Crea una instancia de {@link RoleModel} a partir de un nombre de rol como String.
     * Utiliza el enum {@link ERole} para asignar el valor.
     * @param roleName nombre del rol (debe coincidir con {@link ERole})
     * @return entidad RoleModel con el nombre asignado */

    default RoleModel fromString(String roleName) {
        RoleModel role = new RoleModel();
        role.setName(ERole.valueOf(roleName));
        return role;
    }

    /** Convierte un conjunto de nombres de roles en un conjunto de entidades {@link RoleModel}.
     * @param roleNames conjunto de nombres
     * @return conjunto de entidades RoleModel */

    default Set<RoleModel> fromStringSet(Set<String> roleNames) {
        return roleNames.stream()
                .map(this::fromString)
                .collect(Collectors.toSet());
    }
}



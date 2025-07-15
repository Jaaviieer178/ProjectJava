package com.api.java.mapper;
import com.api.java.dto.UserDTO;
import com.api.java.models.RoleModel;
import com.api.java.models.UserModel;
import org.mapstruct.*;


/** Mapper de usuario que transforma entre {@link UserModel} (entidad JPA) y {@link UserDTO} (objeto de transporte).
 * Este mapper usa la estrategia IGNORE para evitar sobrescribir valores nulos, y delega la conversión de roles
 * al {@link RoleMapper}.*/

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = RoleMapper.class)
public interface UserMapper {

    /** Convierte un {@link UserModel} a un {@link UserDTO}.
     * @param userModel la entidad de usuario obtenida de la base de datos
     * @return el DTO correspondiente, con los mismos campos básicos  */

    @Mapping(source = "role", target = "role")
    UserDTO UserModelToUserDto(UserModel userModel);

    /** Convierte un {@link UserDTO} a un {@link UserModel}, normalmente para creación.
     * Nota: Aunque se mapea el campo "role", en la práctica se ignora este valor, ya que el servicio
     * se encarga de asignar manualmente las entidades {@link RoleModel}.
     * @param userDTO el DTO recibido desde una petición (JSON)
     * @return la entidad de usuario parcialmente armada, lista para completar roles y persistir */

    @Named("UserDtoToUser")
    @Mapping(source = "role", target = "role")
    UserModel UserDtoToUser(UserDTO userDTO);

    /** Actualiza un {@link UserModel} existente con datos provenientes de un {@link UserDTO}.
     * Ignora el campo "id" para evitar sobrescritura, e ignora "role" porque se trata aparte
     * según lógica de negocio (asignación de roles).
     * @param dto     los nuevos datos recibidos en la petición
     * @param entity  la entidad que será actualizada (la que viene de la base) */

    @InheritConfiguration(name = "UserDtoToUser")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget UserModel entity);
}


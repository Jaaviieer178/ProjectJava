package com.api.java.services;
import com.api.java.dto.UserDTO;
import com.api.java.mapper.RoleMapper;
import com.api.java.mapper.UserMapper;
import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import com.api.java.models.UserModel;
import com.api.java.repositories.IRoleRepository;
import com.api.java.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Servicio que gestiona operaciones relacionadas con usuarios */
@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;  // Repositorio de usuarios para acceder a la base de datos
    private final UserMapper userMapper; // Mapper que convierte entre UserModel y UserDTO
    private final IRoleRepository roleRepository; // Repositorio de roles para buscar entidades RoleModel desde la base
    private final RoleMapper roleMapper; // Mapper que convierte entre RoleModel y Strings (como "USER")
    private final PasswordEncoder passwordEncoder;  // Mapper que convierte las contraseñas y en contraseñas encriptadas


    /** Crea un nuevo usuario a partir de los datos recibidos en un UserDTO.
     * @param userDTO Objeto que contiene los datos del nuevo usuario y sus roles como nombres (strings).
     * @return UserDTO con los datos del usuario creado y los roles formateados como texto. */

    public UserDTO createUser(UserDTO userDTO) {
        UserModel user = userMapper.UserDtoToUser(userDTO);     //  Convertimos el DTO recibido a una entidad de tipo UserModel (entidad JPA)
        Set<RoleModel> roles = resolveRolesFromNames(userDTO.getRole());    //  Resolvemos los nombres de los roles a entidades RoleModel válidas desde la base de datos
        user.setRole(roles);     // Asignamos los roles resueltos a la entidad del usuario
        UserModel savedUser = userRepository.save(user);    //  Guardamos el usuario en la base de datos mediante el repositorio
        return mapToDtoWithRoles(savedUser);     //  Convertimos la entidad persistida nuevamente a DTO, incluyendo los roles como strings
    }

    /** Obtiene todos los usuarios registrados.
     * @return una lista de objetos {@link UserDTO} con los datos de cada usuario,
     *  * incluyendo sus roles representados como cadenas de texto.  */

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDtoWithRoles)   // Convierte cada UserModel en UserDTO con roles legibles
                .collect(Collectors.toList());  // Junta toddo en una lista
    }

    /** Busca un usuario por su ID.
     * @param id el ID del usuario
     * @return el DTO del usuario si se encuentra
     * @throws ResponseStatusException si el usuario no existe  */

    public UserDTO getUserById(Long id) {  return mapToDtoWithRoles (throwResponse(id)); } // Si existe, lo transforma a DTO con roles y si no, lanza excepción con 404

    /** Actualiza un usuario existente con nuevos datos.
     * @param id el ID del usuario a actualizar
     * @param updateData los nuevos datos para actualizar
     * @return el usuario actualizado como DTO
     * @throws ResponseStatusException si el usuario no existe */

    @Transactional
    public UserDTO updateById(Long id, UserDTO updateData) {
        UserModel user = throwResponse(id);     //  Busca el usuario por su ID en la base de datos.
        userMapper.updateUserFromDto(updateData, user);     //  Actualiza los campos básicos del usuario con los datos que vienen en el DTO.
        if (shouldUpdateRoles(updateData)) {      //  Verifica si el DTO tiene roles nuevos especificados.
            Set<RoleModel> roles = resolveRolesFromNames(updateData.getRole());
            user.setRole(roles);
        }
        String encondedPass = passwordEncoder.encode(updateData.getPassword());
        user.setPassword(encondedPass);
        return mapToDtoWithRoles(userRepository.save(user));    //  Guarda el usuario actualizado en la base y convierte el resultado a DTO,
    }


    /** Elimina un usuario por su ID.
     * @param id el ID del usuario a eliminar
     * @throws ResponseStatusException si el usuario no existe */

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) { // Verifica si el usuario existe
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría con ID " + id + " no encontrada");
        }
        userRepository.deleteById(id); // Si existe, lo elimina de la base de datos
    }


    // ========================================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ========================================================

    /** Determina si el objeto {@link UserDTO} contiene información de roles para actualizar.
     * @param dto el DTO que se evalúa
     * @return {@code true} si contiene una lista no vacía de nombres de rol, de lo contrario {@code false}  */

    private boolean shouldUpdateRoles(UserDTO dto) {
        return dto.getRole() != null && !dto.getRole().isEmpty();
    }

    /** Convierte un conjunto de nombres de rol en entidades {@link RoleModel}, validando que existan en la base de datos.
     * @param roleNames conjunto de nombres de roles como {@link String}, por ejemplo: "ADMIN", "USER"
     * @return conjunto de entidades {@link RoleModel} correspondientes
     * @throws ResponseStatusException si algún rol no es válido o no se encuentra en la base */

    private Set<RoleModel> resolveRolesFromNames(Set<String> roleNames) {
        return roleNames.stream()   // Inicia un stream sobre los nombres de roles recibidos
                .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName))     // Convierte cada string a un valor del enum ERole
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido: " + roleName)))  // Si no se encuentra en la base, lanza un error 400 con un mensaje claro
                .collect(Collectors.toSet());    // Junta todas las entidades RoleModel encontradas en un Set (evita duplicados)
    }


    /** Convierte una entidad {@link UserModel} en un {@link UserDTO} con los roles convertidos a cadenas legibles.
     * @param user la entidad {@link UserModel} que se quiere mapear
     * @return el {@link UserDTO} resultante con los roles convertidos a texto */

    private UserDTO mapToDtoWithRoles(UserModel user) {
        UserDTO dto = userMapper.UserModelToUserDto(user);   // Convierte la entidad JPA UserModel en un DTO básico usando el mapper.
        dto.setRole(roleMapper.toStringSet(user.getRole()));    //Toma los roles del UserModel y los convierte en strings legibles, luego los asigna al campo 'role' del DTO.
        return dto;     // Devuelve el DTO completamente armado, listo para usarse en respuestas REST.
    }

    /** Metodo reutilizable que obtiene un usuario por su ID o lanza una excepción 404 si no existe.
     * @param id ID del usuario
     * @return entidad encontrada
     * @throws ResponseStatusException si no se encuentra */

    private UserModel throwResponse(Long id) {  return userRepository.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado"));  }
}

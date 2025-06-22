package com.api.java.services;
import com.api.java.dto.UserDTO;
import com.api.java.mapper.UserMapper;
import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import com.api.java.models.UserModel;
import com.api.java.repositories.IRoleRepository;
import com.api.java.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final IRoleRepository roleRepository;


    public UserDTO createUser(UserDTO userDTO) {
        // Validación de roles

        Set<RoleModel> roles = userDTO.getRole().stream()
                .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
                .collect(Collectors.toSet());

        UserModel user = userMapper.UserDtoToUser(userDTO);
        user.setRole(roles);

        /* Encriptar contraseña (si aplica)
        user.setPassword(passwordEncoder.encode(user.getPassword()));*/

        return userMapper.UserModelToUserDto(userRepository.save(user));
    }












    /*
    public UserDTO createUser(UserDTO userDTO) {
        return userMapper.UserModelToUserDto(userRepository.save(userMapper.UserDtoToUser(userDTO)));
    }*/












    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::UserModelToUserDto)
                .collect(Collectors.toList());

    }

    public UserDTO getUserById(Long id) {
        return userMapper.UserModelToUserDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"))
        );
    }

    @Transactional
    public UserDTO updateById(Long id, UserDTO updateData) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        userMapper.updateUserFromDto(updateData, user);
        return userMapper.UserModelToUserDto(userRepository.save(user));
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado");
        }
        userRepository.deleteById(id);
    }
}
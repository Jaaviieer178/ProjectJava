package com.api.java.mapper;

import com.api.java.dto.UserDTO;
import com.api.java.models.UserModel;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = RoleMapper.class)
public interface UserMapper {

    UserDTO UserModelToUserDto(UserModel userModel);

    @Named("UserDtoToUser")
    UserModel UserDtoToUser(UserDTO userDTO);

    @InheritConfiguration(name = "UserDtoToUser")
    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget UserModel entity);

}

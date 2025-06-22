package com.api.java.mapper;

import com.api.java.dto.RoleDTO;

import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    RoleDTO RoleModelToDto(RoleModel role);

    @Named("RoleDtoToRole")
    RoleModel RoleDtoToRole(RoleDTO roleDTO);

    @InheritConfiguration(name = "RoleDtoToRole")
    @Mapping(target = "id", ignore = true)
    void updateRoleFromDto(RoleDTO dto, @MappingTarget RoleModel entity);


    default String toString(RoleModel role) {
        return role.getName().name();
    }
    default Set<String> toStringSet(Set<RoleModel> roles) {
        return roles.stream()
                .map(this::toString)
                .collect(Collectors.toSet());
    }

    default RoleModel fromString(String roleName) {
        RoleModel role = new RoleModel();
        role.setName(ERole.valueOf(roleName));
        return role;
    }

    default Set<RoleModel> fromStringSet(Set<String> roleNames) {
        return roleNames.stream()
                .map(this::fromString)
                .collect(Collectors.toSet());
    }

}



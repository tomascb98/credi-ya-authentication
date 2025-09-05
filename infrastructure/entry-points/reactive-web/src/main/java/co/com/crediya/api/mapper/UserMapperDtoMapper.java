package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.RegisterUserDto;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapperDtoMapper {
    
    @Mapping(target = "role", source = "userRoleId", qualifiedByName = "mapRoleFromId")
    @Mapping(target = "id", ignore = true) // El ID se genera automáticamente
    User mapToEntity(RegisterUserDto registerUserDto);
    
    @Mapping(target = "userRoleId", source = "role.id")
    RegisterUserDto mapToDto(User user);

    @Named("mapRoleFromId")
    default Role mapRoleFromId(Integer userRoleId) {
        if (userRoleId == null) {
            return null;
        }
        return Role.builder()
                .id(userRoleId)
                .build();
    }
}

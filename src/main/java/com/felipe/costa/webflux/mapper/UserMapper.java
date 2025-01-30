package com.felipe.costa.webflux.mapper;


import com.felipe.costa.webflux.entity.User;
import com.felipe.costa.webflux.model.request.UserRequest;
import com.felipe.costa.webflux.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity (final UserRequest request);

    @Mapping(target = "id", ignore = true)
    User toEntity (final UserRequest request, @MappingTarget final User entity);

    UserResponse toResponse (final User entity);

}

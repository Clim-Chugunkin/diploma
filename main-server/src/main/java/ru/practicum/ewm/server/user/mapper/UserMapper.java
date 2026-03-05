package ru.practicum.ewm.server.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.user.DTO.NewUserRequest;
import ru.practicum.ewm.server.user.DTO.UserDto;
import ru.practicum.ewm.server.user.DTO.UserShortDto;
import ru.practicum.ewm.server.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    User toUser(NewUserRequest request);
}

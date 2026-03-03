package ru.practicum.ewm.server.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.user.DTO.NewUserRequest;
import ru.practicum.ewm.server.user.DTO.UserDto;
import ru.practicum.ewm.server.user.DTO.UserShortDto;
import ru.practicum.ewm.server.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    //    public static UserDto fromUserToUserDto(User user) {
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        return userDto;
//    }
    UserDto toUserDto(User user);

    //
//    public static UserShortDto fromUserToUserShortDto(User user) {
//        UserShortDto userShortDto = new UserShortDto();
//        userShortDto.setId(user.getId());
//        userShortDto.setName(user.getName());
//        return userShortDto;
//    }
    UserShortDto toUserShortDto(User user);

    User toUser(NewUserRequest request);
//
//    public static User fromNewUserRequestToUser(NewUserRequest request) {
//        User user = new User();
//        user.setEmail(request.getEmail());
//        user.setName(request.getName());
//        return user;
//    }

}

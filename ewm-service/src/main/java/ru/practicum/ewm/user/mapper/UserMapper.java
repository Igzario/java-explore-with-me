package ru.practicum.ewm.user.mapper;

import lombok.experimental.UtilityClass;
import org.mapstruct.Mapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring")
public interface  UserMapper {

    UserShortDto toUserShortDto(User user);
    UserDto toUserDto(User user);
    User toDtoUser(UserDto userDto);


//    public UserDto toUserDto(User user) {
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setEmail(user.getEmail());
//        userDto.setName(user.getName());
//        return userDto;
//    }
//
//    public User toDtoUser(UserDto userDto) {
//        User newUser = new User();
//        newUser.setName(userDto.getName());
//        newUser.setEmail(userDto.getEmail());
//        return newUser;
//    }
}
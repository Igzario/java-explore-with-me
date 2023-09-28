package ru.practicum.ewm.user;

import ru.practicum.ewm.exception.exceptions.EmailAlreadyExists;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    ArrayList<UserDto> findAllUsers(List<Long> idList, int from, int size);

    void deleteUser(Long id) throws EntityNotFoundException;

    UserDto addNewUser(UserDto userDto) throws NameAlreadyExists, EmailAlreadyExists;

    User getUserById(Long userId) throws EntityNotFoundException;
}
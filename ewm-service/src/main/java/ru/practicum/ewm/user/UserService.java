package ru.practicum.ewm.user;

import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EmailAlreadyExists;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    void deleteUser(Long id) throws EntityNotFoundException;

    UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists;

    @SneakyThrows
    @Transactional(readOnly = true)
    User getUserById(Long userId);
}
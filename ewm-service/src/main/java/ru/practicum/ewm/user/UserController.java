package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.exception.EmailAlreadyExists;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> idList,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на вывод спика пользователей");
        return userService.getAllUsers(idList, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@Valid @RequestBody UserDto userDto) throws EmailAlreadyExists {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userService.addNewUser(userDto);
    }


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) throws EntityNotFoundException {
        log.info("Запрос на удаление пользователя с ID {}", userId);
        userService.deleteUser(userId);
    }
}
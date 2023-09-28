package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.exception.exceptions.EmailAlreadyExists;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.NameAlreadyExists;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAllUsers(@RequestParam(value = "ids", required = false) List<Long> idList,
                                      @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request to display a list of users");
        return userService.findAllUsers(idList, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@Valid @RequestBody UserDto userDto) throws NameAlreadyExists, EmailAlreadyExists {
        log.info("Request to add a user {}", userDto);
        return userService.addNewUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) throws EntityNotFoundException {
        log.info("Request to delete a user with ID-{}", userId);
        userService.deleteUser(userId);
    }
}
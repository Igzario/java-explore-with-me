package ru.practicum.ewm.event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.exception.EmailAlreadyExists;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {

}

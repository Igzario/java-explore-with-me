package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addNewEvent(@PathVariable Long userId,
                             @Valid @RequestBody NewEventDto event) {
        log.info("Запрос на добавление события от пользователя с ID-{}: {}", userId, event);
        return eventService.addNewEvent(userId, event);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<EventShortDto> findEventsByInitiatorId(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос вывод событий, где инициатор User c ID-{}", userId);
        return eventService.findEventsByInitiator(userId, from, size);
    }

    @GetMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto findEventByUserIdAndEventId(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info("Запрос вывод события с ID-{} , где инициатор User c ID-{}", eventId, userId);
        return eventService.findEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto updateEvent(@PathVariable Long userId,
                             @Valid @RequestBody UpdateEventUserRequest event,
                             @PathVariable Long eventId) {
        log.info("Запрос обновление события c ID-{} от User c ID-{}, обновление - {}", eventId, userId, event);
        return eventService.updateEvent(userId, event, eventId);
    }


}
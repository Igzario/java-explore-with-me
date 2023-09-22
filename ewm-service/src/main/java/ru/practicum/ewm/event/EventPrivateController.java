package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    EventFullDto addNewEvent(@PathVariable Long userId,
                             @RequestBody NewEventDto event) {
        log.info("Запрос на добавление события от пользователя с ID {}: {}", userId, event);
        return eventService.addNewEvent(userId, event);
    }

    @GetMapping
    List<EventShortDto> findEventsByInitiatorId(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос вывод событий, где инициатор User c ID: {}", userId);
        return eventService.findEventsByInitiator(userId, from, size);
    }


    @GetMapping(value = "/{eventId}")
    EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Запрос вывод события с ID: {} , где инициатор User c ID: {}", eventId, userId);
        return eventService.findEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    EventFullDto updateEvent(@PathVariable Long userId,
                             @RequestBody NewEventDto event,
                             @PathVariable Long eventId) {
        EventFullDto dto = eventService.updateEvent(userId, event, eventId);
        log.info("Обновлено событие, {}", dto);
        return dto;
    }
}
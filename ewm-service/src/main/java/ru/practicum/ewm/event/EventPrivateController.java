package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.EventDateException;
import ru.practicum.ewm.exception.exceptions.UserNotInitiatorEventException;
import ru.practicum.ewm.exception.exceptions.WrongStateForUpdateEvent;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto event)
            throws EntityNotFoundException, EventDateException {
        log.info("Request to add an event from a user with ID-{}: {}", userId, event);
        return eventService.addNewEvent(userId, event);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEventsByInitiatorId(@PathVariable Long userId,
                                                       @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                       @Valid @Positive @RequestParam(defaultValue = "10") int size)
            throws EntityNotFoundException {
        log.info("Request output of events, where the initiator is User with ID-{}", userId);
        return Collections.unmodifiableList(eventService.findEventsByInitiator(userId, from, size));
    }

    @GetMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventByUserIdAndEventId(@PathVariable Long userId,
                                                    @PathVariable Long eventId) throws EntityNotFoundException {
        log.info("Request output event with ID-{} , where the initiator is User with ID-{}", eventId, userId);
        return eventService.findEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @Valid @RequestBody UpdateEventUserRequest event,
                                    @PathVariable Long eventId)
            throws WrongStateForUpdateEvent, UserNotInitiatorEventException,
            EntityNotFoundException, EventDateException {
        log.info("Request update event c ID-{} from User c ID-{}, update - {}", eventId, userId, event);
        return eventService.updateEvent(userId, event, eventId);
    }
}
package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.utility.State;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEventsAdmin(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<State> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Admin: запрос на вывод списка событий: {}, {}, {}, {}, {}",
                users, states, categories, rangeStart, rangeEnd);
        return eventService.findEventsAdmin(users,
                states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto adminUpdateEvent(@PathVariable Long eventId,
                                         @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        EventFullDto dto = eventService.adminUpdateEvent(eventId, eventDto);
        log.info("Admin: запрос на обновление события с ID-{}, обвновление - {}", eventId, dto);
        return dto;
    }
}
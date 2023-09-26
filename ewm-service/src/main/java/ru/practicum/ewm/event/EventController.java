package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.HitClient;
import ru.practicum.ewm.client.dto.EndPointHit;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.EventNotPublishedException;
import ru.practicum.ewm.exception.exceptions.IncorrectRequestException;
import ru.practicum.ewm.utility.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {

    private final EventService eventService;
    private final HitClient hitClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEvents(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(required = false) String sort,
                                          @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Valid @Positive @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) throws IncorrectRequestException {
        log.info("Admin: request to display a list of events: {}, {}, {}, {}, {}, {}, {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        saveHit(request);
        return eventService.findEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request)
            throws EntityNotFoundException, EventNotPublishedException {
        log.info("Admin: request to display an event with ID-: {}", eventId);
        saveHit(request);
        return eventService.findEventById(eventId);
    }

    private void saveHit(HttpServletRequest request) {
        hitClient.addHit(EndPointHit.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(Constants.TIME_FORMATTER))
                .build());
        log.info("Admin: request sent to statistics server: {}", request);
    }
}
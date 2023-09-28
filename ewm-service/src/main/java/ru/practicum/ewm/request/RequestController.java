package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.ValidationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping(value = "/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId)
            throws ValidationException, EntityNotFoundException {
        log.info("Request to add a request for participation to an event with ID-{}, from User with ID-{}", eventId, userId);
        return requestService.addNewRequest(userId, eventId);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findAllRequestsByUserId(@PathVariable long userId) {
        log.info("Request to display requests for participation in events from User with ID-{}", userId);
        return requestMapper.requestDtoList(requestService.findAllRequestsByUserId(userId));
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(@PathVariable long userId, @PathVariable long requestId)
            throws ValidationException, EntityNotFoundException {
        log.info("Request to cancel a request to participate in an event with ID-{} from User with ID-{}",
                requestId, userId);
        return requestService.canceledRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllByUserAndEvent(@PathVariable long userId,
                                                               @PathVariable long eventId)
            throws ValidationException, EntityNotFoundException {
        log.info("Request to display a list of requests for participation in an event with ID-{} from User with ID-{}",
                eventId, userId);
        return requestService.findAllByUserAndEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(@PathVariable long userId,
                                                              @PathVariable long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request)
            throws ValidationException, EntityNotFoundException {
        log.info("Request to change a request to participate in an event with ID-{} from User with ID-{}",
                eventId, userId);
        return requestService.updateStatusRequest(request, userId, eventId);
    }
}

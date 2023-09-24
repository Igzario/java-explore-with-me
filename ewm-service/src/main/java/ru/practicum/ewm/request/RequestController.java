package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
                                                 @RequestParam Long eventId) {
        log.info("Запрос на добавление запроса на участие к событию с ID-{}, от User с ID-{}", eventId, userId);
        return requestService.addNewRequest(userId, eventId);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findAllRequestsByUserId(@PathVariable long userId) {
        log.info("Запрос на вывод запросов на участие в событиях от User с ID-{}", userId);
        return requestMapper.requestDtoList(requestService.findAllRequestsByUserId(userId));
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("Запрос на отмену запроса на участие в событии с ID-{} от User с ID-{}", requestId, userId);
        return requestService.canceledRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllByUserAndEvent(@PathVariable long userId,
                                                               @PathVariable long eventId) {
        log.info("Запрос на вывод списка запросов на участие в событии с ID-{} от User с ID-{}", eventId, userId);
        return requestService.findAllByUserAndEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(@PathVariable long userId,
                                                              @PathVariable long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Запрос на изменение запроса на участие в событии с ID-{} от User с ID-{}", eventId, userId);
        return requestService.updateStatusRequest(request, userId, eventId);
    }
}

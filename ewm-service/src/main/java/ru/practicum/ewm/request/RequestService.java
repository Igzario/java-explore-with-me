package ru.practicum.ewm.request;

import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.ValidationException;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utility.State.*;
import static ru.practicum.ewm.utility.State.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventService eventService;
    private String error;

    public List<Request> findAllRequestsByUserId(long userId) {
        log.info("Возвращен список запрососв на участие в событиях от User с ID-{}", userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    @SneakyThrows
    @Transactional
    public ParticipationRequestDto addNewRequest(Long userId, Long eventId) {
        User requester = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        List<Request> list = findAllRequestsByUserId(userId);

        List<Long> listIdEvents = new ArrayList<>();
        list.forEach(request -> listIdEvents.add(request.getEvent().getId()));

        if (listIdEvents.contains(eventId)) {
            error = "Нельзя добавить повторный запрос";
            log.error("Сгененрирован ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (event.getInitiator().getId() == userId) {
            error = "Инициатор события не может добавить запрос на участие в своём событии";
            log.error("Сгененрирован ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (!event.getState().equals(PUBLISHED)) {
            error = "Нельзя участвовать в неопубликованном событии";
            log.error("Сгененрирован ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit()
                == event.getConfirmedRequests())) {
            error = "Достигнут лимит запросов на участие";
            log.error("Сгененрирован ValidationException: {}", error);
            throw new ValidationException(error);
        }

        Request request = new Request();
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.saveAndFlush(event);
        }
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);

        requestRepository.saveAndFlush(request);
        log.info("Добавлено запрос на участие в событии: {}", request);
        return requestMapper.requestToDto(request);
    }

    @SneakyThrows
    @Transactional
    public ParticipationRequestDto canceledRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(Request.class, requestId));
        Event event = request.getEvent();

        if (request.getRequester().getId() != userId) {
            error = "запрос на событие не принадлежит пользователю";
            throw new ValidationException(error);
        }
        request.setStatus(CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.saveAndFlush(event);
        requestRepository.saveAndFlush(request);
        log.debug("Запрос на участие в событии отменен: {}", request);
        return requestMapper.requestToDto(request);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findAllByUserAndEvent(long userId, long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event.getInitiator().getId() != userId) {
            error = "Событие не принадлежит пользователю";
            throw new ValidationException(error);
        }
        List<Request> list = requestRepository.findAllByEventId(eventId);
        log.debug("Выведен список заявок на участи в событии с ID-{}: {}",
                eventId, list);
        return requestMapper.requestDtoList(list);
    }

    @SneakyThrows
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(EventRequestStatusUpdateRequest updateRequest,
                                                              long userId, long eventId) {
        final List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        final List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        ;
        for (Long requestId : updateRequest.getRequestIds()) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException(Request.class, requestId));
            Event event = request.getEvent();

            if (event.getInitiator().getId() != userId) {
                error = "Событие не принадлежит пользователю";
                log.error("Сгененрирован ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (request.getRequester().getId() == userId) {
                error = "Нельзя подтверждать свой запрос";
                log.error("Сгененрирован ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (request.getStatus().equals(CONFIRMED)) {
                error = "Подтверждение не требуется";
                log.error("Сгененрирован ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                error = "Достигнут лимит запросов на участие";
                log.error("Сгененрирован ValidationException: {}", error);
                throw new ValidationException(error);
            }
            switch (updateRequest.getStatus()) {
                case CONFIRMED:
                    request.setStatus(CONFIRMED);
                    confirmedRequests.add(requestMapper.requestToDto(request));
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRepository.saveAndFlush(event);
                    break;
                case REJECTED:
                    request.setStatus(REJECTED);
                    rejectedRequests.add(requestMapper.requestToDto(request));
                    break;
            }
        }
        log.error("Возвращен список запросов после обновления:\n подтвержденные:{} \n отклоненные:{}",
                confirmedRequests, rejectedRequests);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
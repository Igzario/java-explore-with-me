package ru.practicum.ewm.request;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventServiceImpl;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.ValidationException;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.UserServiceImpl;
import ru.practicum.ewm.user.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utility.State.*;
import static ru.practicum.ewm.utility.State.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;
    private final UserServiceImpl userService;
    private final EventServiceImpl eventService;
    private String error;

    @Override
    @Transactional
    public List<Request> findAllRequestsByUserId(long userId) {
        log.info("Returned a list of requests for participation in events from User with ID-{}", userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    @Transactional
    public ParticipationRequestDto addNewRequest(Long userId, Long eventId)
            throws EntityNotFoundException, ValidationException {
        User requester = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        List<Request> list = findAllRequestsByUserId(userId);

        List<Long> listIdEvents = new ArrayList<>();
        list.forEach(request -> listIdEvents.add(request.getEvent().getId()));

        if (listIdEvents.contains(eventId)) {
            error = "Cannot add repeat request";
            log.error("Generated ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (event.getInitiator().getId().equals(userId)) {
            error = "The event initiator cannot add a request to participate in his event";
            log.error("Generated ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (!event.getState().equals(PUBLISHED)) {
            error = "You cannot participate in an unpublished event";
            log.error("Generated ValidationException: {}", error);
            throw new ValidationException(error);
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit().equals(event.getConfirmedRequests()))) {
            error = "Participation request limit reached";
            log.error("Generated ValidationException: {}", error);
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
        log.info("Added a request to participate in the event: {}", request);
        return requestMapper.requestToDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto canceledRequest(long userId, long requestId)
            throws EntityNotFoundException, ValidationException {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(Request.class, requestId));
        Event event = request.getEvent();

        if (request.getRequester().getId() != userId) {
            error = "The event request does not belong to the user";
            throw new ValidationException(error);
        }
        request.setStatus(CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.saveAndFlush(event);
        requestRepository.saveAndFlush(request);
        log.debug("Event request canceled: {}", request);
        return requestMapper.requestToDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findAllByUserAndEvent(long userId, long eventId)
            throws EntityNotFoundException, ValidationException {
        Event event = eventService.getEventById(eventId);
        if (event.getInitiator().getId() != userId) {
            error = "The event does not belong to the user";
            throw new ValidationException(error);
        }
        List<Request> list = requestRepository.findAllByEventId(eventId);
        log.debug("A list of applications for participation in the event with ID-{} is displayed: {}",
                eventId, list);
        return requestMapper.requestDtoList(list);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(EventRequestStatusUpdateRequest updateRequest,
                                                              long userId, long eventId)
            throws EntityNotFoundException, ValidationException {
        final List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        final List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Long requestId : updateRequest.getRequestIds()) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException(Request.class, requestId));
            Event event = request.getEvent();

            if (event.getInitiator().getId() != userId) {
                error = "The event does not belong to the user";
                log.error("Generated ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (request.getRequester().getId() == userId) {
                error = "You cannot confirm your request";
                log.error("Generated ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (request.getStatus().equals(CONFIRMED)) {
                error = "No confirmation required";
                log.error("Generated ValidationException: {}", error);
                throw new ValidationException(error);
            }
            if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                error = "Participation request limit reached";
                log.error("Generated ValidationException: {}", error);
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
        log.error("Returned list of requests after update:\n confirmed:{} \n rejected:{}",
                confirmedRequests, rejectedRequests);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
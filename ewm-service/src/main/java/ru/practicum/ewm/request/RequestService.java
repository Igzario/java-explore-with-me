package ru.practicum.ewm.request;

import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exception.exceptions.ValidationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> findAllRequestsByUserId(long userId);

    ParticipationRequestDto addNewRequest(Long userId, Long eventId)
            throws EntityNotFoundException, ValidationException;

    ParticipationRequestDto canceledRequest(long userId, long requestId)
            throws EntityNotFoundException, ValidationException;

    List<ParticipationRequestDto> findAllByUserAndEvent(long userId, long eventId)
            throws EntityNotFoundException, ValidationException;

    EventRequestStatusUpdateResult updateStatusRequest(EventRequestStatusUpdateRequest updateRequest,
                                                       long userId, long eventId)
            throws EntityNotFoundException, ValidationException;
}
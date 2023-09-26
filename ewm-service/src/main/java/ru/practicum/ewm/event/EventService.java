package ru.practicum.ewm.event;

import lombok.SneakyThrows;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.*;
import ru.practicum.ewm.utility.State;

import java.util.List;

public interface EventService {
    EventFullDto addNewEvent(Long userId, NewEventDto eventDto) throws EventDateException, EntityNotFoundException;

    List<EventShortDto> findEventsByInitiator(Long userId, int from, int size) throws EntityNotFoundException;

    EventFullDto findEventByUserIdAndEventId(Long userId, Long eventId) throws EntityNotFoundException;

    EventFullDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId)
            throws EntityNotFoundException, WrongStateForUpdateEvent,
            UserNotInitiatorEventException, EventDateException;

    @SneakyThrows
    List<EventShortDto> findEvents(String text, List<Long> categories, Boolean paid,
                                   String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                   String sort, int from, int size) throws IncorrectRequestException;

    EventFullDto findEventById(Long eventId) throws EntityNotFoundException, EventNotPublishedException;

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest eventDto)
            throws EntityNotFoundException, EventDateException, EventStatusUpdateException;

    List<EventFullDto> findEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                       String rangeStart, String rangeEnd, int from, int size);

    Event getEventById(Long eventId) throws EntityNotFoundException;


}
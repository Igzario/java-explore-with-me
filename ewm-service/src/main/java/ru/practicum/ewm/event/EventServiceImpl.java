package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryServiceImpl;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.*;
import ru.practicum.ewm.utility.State;
import ru.practicum.ewm.user.UserServiceImpl;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm.utility.State.*;
import static ru.practicum.ewm.utility.StateAction.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryServiceImpl categoryService;
    private final UserServiceImpl userService;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) throws EventDateException, EntityNotFoundException {
        Event event = eventMapper.toEventFromNew(eventDto);
        if (!validEventDate(event.getEventDate())) {
            log.error("Generated EventDateException");
            throw new EventDateException();
        }
        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        event.setCategory(categoryService.getCategoryById(eventDto.getCategory()));
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userService.getUserById(userId));
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);

        event = eventRepository.save(event);
        eventRepository.saveAndFlush(event);
        log.info("Event added: {}", event);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findEventsByInitiator(Long userId, int from, int size) throws EntityNotFoundException {
        int start = from / size;
        Pageable pageable = PageRequest.of(start, size);
        User user = userService.getUserById(userId);
        List<Event> events = eventRepository.findAllByInitiator(user, pageable);
        log.info("Listing events: {}", events);
        return eventMapper.toShortEventDtoList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventByUserIdAndEventId(Long userId, Long eventId) throws EntityNotFoundException {
        userService.getUserById(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId)
            throws EntityNotFoundException, WrongStateForUpdateEvent, UserNotInitiatorEventException, EventDateException {
        userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (event.getState() == State.UNSUPPORTED_STATE
                || event.getState() == PUBLISHED) {
            log.error("Generated WrongStateForUpdateEvent");
            throw new WrongStateForUpdateEvent();
        }
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            log.error("Generated UserNotInitiatorEventException" + "UserId-" + userId + "EventId-" + eventId);
            throw new UserNotInitiatorEventException(userId, eventId);
        }
        if (eventDto.getAnnotation() != null) {
            if (!eventDto.getAnnotation().isBlank()) {
                event.setAnnotation(eventDto.getAnnotation());
            }
        }
        if (eventDto.getDescription() != null) {
            if (!eventDto.getDescription().isBlank()) {
                event.setDescription(eventDto.getDescription());
            }
        }
        if (eventDto.getEventDate() != null) {
            if (!validEventDate(LocalDateTime.parse(eventDto.getEventDate(), Constants.TIME_FORMATTER))) {
                log.error("Generated EventDateException");
                throw new EventDateException();
            }
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), Constants.TIME_FORMATTER));
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getStateAction() == CANCEL_REVIEW) {
            event.setState(CANCELED);
        }
        if (eventDto.getStateAction() == SEND_TO_REVIEW) {
            event.setState(PENDING);
        }
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findEvents(String text, List<Long> categories, Boolean paid,
                                          String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                          String sort, int from, int size) throws IncorrectRequestException {
        if (categories.size() == 1 && categories.get(0) == 0) {
            log.error("Generated IncorrectRequestException");
            throw new IncorrectRequestException();
        }
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.now().plusYears(10);
        } else {
            start = LocalDateTime.parse(rangeStart, Constants.TIME_FORMATTER);
            end = LocalDateTime.parse(rangeEnd, Constants.TIME_FORMATTER);
        }

        Sort srt = Sort.unsorted();
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    srt = Sort.by(Sort.Direction.DESC, "eventDate");
                    break;
                case "VIEWS":
                    srt = Sort.by(Sort.Direction.DESC, "views");
                    break;
            }
        }
        Pageable pageable = PageRequest.of(from / size, size, srt);
        List<Event> events;
        if (paid == null) {
            events = eventRepository.findEventsWithoutText(categories, paid, start, end, onlyAvailable, pageable);
        }
        if (text == null) {
            if (paid == null) {
                events = eventRepository.findEventsWithoutTextAndPaid(categories, start, end, onlyAvailable, pageable);
            } else {
                events = eventRepository.findEventsWithoutText(categories, paid, start, end, onlyAvailable, pageable);
            }
        } else {
            if (paid == null) {
                events = eventRepository.findEventsWithoutPaid(text.toLowerCase(),
                        categories, start, end, onlyAvailable, pageable);
            }
            events = eventRepository.findEvents(text.toLowerCase(),
                    categories, paid, start, end, onlyAvailable, pageable);
        }
        events.forEach(event -> event.setViews(event.getViews() + 1));
        log.info("Listing events {} ", events);
        return eventMapper.toShortEventDtoList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventById(Long eventId) throws EntityNotFoundException, EventNotPublishedException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (!event.getState().equals(PUBLISHED)) {
            log.error("Generated EventNotPublishedException");
            throw new EventNotPublishedException();
        }
        log.info("Listing event {} ", event);
        event.setViews(event.getViews() + 1);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest eventDto)
            throws EntityNotFoundException, EventDateException, EventStatusUpdateException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (eventDto.getEventDate() != null) {
            if (!validEventDate(eventDto.getEventDate())) {
                log.error("Generated EventDateException");
                throw new EventDateException();
            }
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventDto.getCategory()));
        }
        if (eventDto.getLocation() != null) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState() != PUBLISHED && event.getState() != REJECTED) {
                        event.setState(PUBLISHED);
                        break;
                    } else {
                        log.info("Generated EventAlreadyPublishedException:" + event.getState());
                        throw new EventStatusUpdateException(event.getState());
                    }
                case REJECT_EVENT:
                    if (event.getState() != PUBLISHED) {
                        event.setState(REJECTED);
                        break;
                    } else {
                        log.info("Generated EventAlreadyPublishedException:" + event.getState());
                        throw new EventStatusUpdateException(event.getState());
                    }
            }
        }
        event = eventRepository.saveAndFlush(event);
        log.info("Admin: event updated {}", event);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                              String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.now().plusYears(500);
        } else {
            start = LocalDateTime.parse(rangeStart, Constants.TIME_FORMATTER);
            end = LocalDateTime.parse(rangeEnd, Constants.TIME_FORMATTER);
        }

        if (states == null) {
            states = new ArrayList<>();
            states.add(PENDING);
            states.add(CANCELED);
            states.add(PUBLISHED);
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = new ArrayList<>();
        if (categories == null && users == null) {
            events = eventRepository.adminFindEventsWithoutCategoriesAndUsers(states, start, end, pageable);
        } else if (categories == null) {
            events = eventRepository.adminFindEventsWithoutCategories(users, states, start, end, pageable);
        } else if (users == null) {
            events = eventRepository.adminFindEventsWithoutUsers(states, categories, start, end, pageable);
        } else {
            events = eventRepository.adminFindEvents(users, states, categories, start, end, pageable);

        }
        events.forEach(event -> event.setViews(event.getViews() + 1));
        log.info("Admin: a list of events is displayed {} ", events);
        return eventMapper.toFullEventDtoList(events);
    }

    private boolean validEventDate(LocalDateTime eventDate) {
        return eventDate.minusHours(2).isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long eventId) throws EntityNotFoundException {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
    }
}

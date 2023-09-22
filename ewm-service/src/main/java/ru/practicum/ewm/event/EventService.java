package ru.practicum.ewm.event;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.utility.State;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.UserNotInitiatorEventException;
import ru.practicum.ewm.exception.WrongStateForUpdateEvent;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventService {
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventMapper eventMapper;

    @SneakyThrows
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        Event event = eventMapper.toEventFromNew(eventDto);
        event.setCategory(categoryService.getCategoryById(eventDto.getCategory()));
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userService.getUserById(userId));
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        event = eventRepository.save(event);
        eventRepository.saveAndFlush(event);
        log.info("Добавлено событие: {}", event);
        return eventMapper.toFullEventDto(event);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<EventShortDto> findEventsByInitiator(Long userId, int from, int size) {
        int start = from / size;
        Pageable pageable = PageRequest.of(start, size);
        User user = userService.getUserById(userId);
        List<Event> events = eventRepository.findAllByInitiator(user, pageable);
        log.info("Вывод списка событий событий: {}", events);
        return eventMapper.toShortEventDtoList(events);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public EventFullDto findEventByUserIdAndEventId(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return eventMapper.toFullEventDto(event);
    }

    @SneakyThrows
    @Transactional
    public EventFullDto updateEvent(Long userId, NewEventDto eventDto, Long eventId) {
        userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        if (event.getState() == State.REJECTED || event.getState() == State.UNSUPPORTED_STATE
                || event.getState() == State.PUBLISHED) {
            throw new WrongStateForUpdateEvent();
        }
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new UserNotInitiatorEventException(userId, eventId);
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
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
        event = eventRepository.save(event);
        return eventMapper.toFullEventDto(event);
    }
}

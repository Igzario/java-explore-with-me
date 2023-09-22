package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapperTest {
//    private CategoryMapper categoryMapper = new CategoryMapperImpl();
//    private UserMapper userMapper = new UserMapperImpl();
//
//    public Event toEventFromNew(NewEventDto eventDto) {
//        return Event.builder()
//                .annotation(eventDto.getAnnotation())
//                .description(eventDto.getDescription())
//                .eventDate(LocalDateTime.parse(eventDto.getEventDate(), Constants.TIME_FORMATTER))
//                .lat(eventDto.getLocation().getLat())
//                .lon(eventDto.getLocation().getLon())
//                .paid(eventDto.getPaid())
//                .participantLimit(eventDto.getParticipantLimit())
//                .requestModeration(eventDto.getRequestModeration())
//                .title(eventDto.getTitle())
//                .build();
//    }
//
//    public static EventShortDto toEventShortDto(Event event) {
//        return EventShortDto.builder()
//                .annotation(event.getAnnotation())
//                .category(categoryMapper.toCategoryDto(event.getCategory()))
//                .confirmedRequests(event.getConfirmedRequests())
//                .eventDate(event.getEventDate().format(Constants.TIME_FORMATTER))
//                .id(event.getId())
//                .initiator(userMapper.toUserShortDto(event.getInitiator()))
//                .paid(event.getPaid())
//                .title(event.getTitle())
//                .build();
//    }
//
//    public EventFullDto toFullEventDto(Event event) {
//        return EventFullDto.builder()
//                .annotation(event.getAnnotation())
//                .category(categoryMapper.toCategoryDto(event.getCategory()))
//                .confirmedRequests(event.getConfirmedRequests())
//                .createdOn(event.getPublishedOn().format(Constants.TIME_FORMATTER))
//                .description(event.getDescription())
//                .eventDate(event.getEventDate().format(Constants.TIME_FORMATTER))
//                .id(event.getId())
//                .initiator(userMapper.toUserShortDto(event.getInitiator()))
//                .location(new Location(event.getLat(), event.getLon()))
//                .paid(event.getPaid())
//                .participantLimit(event.getParticipantLimit())
//                .publishedOn(event.getPublishedOn().format(Constants.TIME_FORMATTER))
//                .requestModeration(event.getRequestModeration())
//                .state(event.getState())
//                .title(event.getTitle())
//                .build();
//    }
//
//    public static List<EventShortDto> toShortEventDtoList(Iterable<Event> events) {
//        List<EventShortDto> result = new ArrayList<>();
//        for (Event event : events) {
//            result.add(toEventShortDto(event));
//        }
//        return result;
//    }
//
//    public static List<EventFullDto> toFullEventDtoList(Iterable<Event> events) {
//        List<EventFullDto> result = new ArrayList<>();
//        for (Event event : events) {
//            result.add(toFullEventDto(event));
//        }
//        return result;
//    }
//
//    public static Set<EventShortDto> toShortEventDtoSet(Iterable<Event> events) {
//        Set<EventShortDto> result = new HashSet<>();
//        for (Event event : events) {
//            result.add(toEventShortDto(event));
//        }
//        return result;
//    }
}

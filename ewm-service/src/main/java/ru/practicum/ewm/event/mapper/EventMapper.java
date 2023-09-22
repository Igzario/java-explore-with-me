package ru.practicum.ewm.event.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.utility.Location;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.utility.Constants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;



@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class}, imports = {Constants.class, LocalDateTime.class, Location.class})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "lat", expression = "java(eventDto.getLocation().getLat())")
    @Mapping(target = "lon", expression = "java(eventDto.getLocation().getLon())")
    @Mapping(target = "eventDate", expression = "java(LocalDateTime.parse(eventDto.getEventDate(), Constants.TIME_FORMATTER))")
    Event toEventFromNew(NewEventDto eventDto);

    @Mapping(target = "eventDate", expression = "java(event.getEventDate().format(Constants.TIME_FORMATTER))")
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "eventDate", expression = "java(event.getEventDate().format(Constants.TIME_FORMATTER))")
    @Mapping(target = "createdOn", expression = "java(event.getPublishedOn().format(Constants.TIME_FORMATTER))")
    @Mapping(target = "location", expression = "java(new Location(event.getLat(), event.getLon()))")
    @Mapping(target = "publishedOn", expression = "java(event.getPublishedOn().format(Constants.TIME_FORMATTER))")
    EventFullDto toFullEventDto(Event event);


    List<EventShortDto> toShortEventDtoList(Iterable<Event> events);
    List<EventFullDto> toFullEventDtoList(Iterable<Event> events);
    Set<EventShortDto> toShortEventDtoSet(Iterable<Event> events);
}

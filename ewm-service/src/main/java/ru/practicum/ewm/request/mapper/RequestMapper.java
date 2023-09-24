package ru.practicum.ewm.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.utility.Constants;
import ru.practicum.ewm.utility.Location;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class, UserMapper.class},
        imports = {Constants.class, LocalDateTime.class, Location.class})
public interface RequestMapper {

    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(request.getRequester().getId())")
    ParticipationRequestDto requestToDto(Request request);

    List<ParticipationRequestDto> requestDtoList(Iterable<Request> requests);
}

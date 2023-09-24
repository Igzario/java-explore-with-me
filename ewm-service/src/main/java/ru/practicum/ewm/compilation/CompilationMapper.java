package ru.practicum.ewm.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.utility.Constants;
import ru.practicum.ewm.utility.Location;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class},
        imports = {Constants.class, LocalDateTime.class, Location.class})
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto dto);

    CompilationDto toCompilationDto(Compilation comp);

    List<CompilationDto> toCompDtoList(Iterable<Compilation> compilations);
}
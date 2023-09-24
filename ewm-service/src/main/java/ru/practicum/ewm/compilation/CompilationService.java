package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Transactional
    public CompilationDto addNewCompilation(NewCompilationDto dto) {
        Compilation compilation = compilationMapper.toCompilation(dto);
        if (dto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(dto.getEvents());
            compilation.setEvents(events);
        }
        if (dto.getPinned() == null) {
            compilation.setPinned(false);
        }
        compilationRepository.saveAndFlush(compilation);
        log.info("Сохранена подборка: {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @SneakyThrows
    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        CompilationDto dto = compilationMapper.toCompilationDto(compilation);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка: {}", compilation);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public CompilationDto findCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));
        return compilationMapper.toCompilationDto(compilation);
    }

    @SneakyThrows
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto dto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(Compilation.class, compId));

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }
        if (dto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(dto.getEvents());
            compilation.setEvents(events);
        }
        compilationRepository.saveAndFlush(compilation);
        log.info("Обновлена подборка: {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    public List<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = new ArrayList<>();
        if (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAllBy(pageable);
        }
        return compilationMapper.toCompDtoList(compilations);
    }
}

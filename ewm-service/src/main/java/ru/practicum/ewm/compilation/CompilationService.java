package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import java.util.List;

public interface CompilationService {
    CompilationDto addNewCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId) throws EntityNotFoundException;

    CompilationDto findCompilationById(Long compId) throws EntityNotFoundException;

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto dto) throws EntityNotFoundException;

    List<CompilationDto> findCompilations(Boolean pinned, int from, int size);
}

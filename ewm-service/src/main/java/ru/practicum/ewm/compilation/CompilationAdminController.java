package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto addNewCompilation(@Valid @RequestBody NewCompilationDto dto) {
        log.info("Request to create a collection: {}", dto);
        return compilationService.addNewCompilation(dto);
    }

    @DeleteMapping(value = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long compId) throws EntityNotFoundException {
        log.info("Request to delete a selection with ID-{}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(value = "/{compId}")
    CompilationDto updateCompilation(@PathVariable Long compId,
                                     @Valid @RequestBody UpdateCompilationDto dto) throws EntityNotFoundException {
        log.info("Запрос на обновление подборки с ID-{}, {}", compId, dto);
        return compilationService.updateCompilation(compId, dto);
    }
}

package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.exception.exceptions.EntityNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping(value = "/{compId}")
    @ResponseStatus(HttpStatus.OK)
    CompilationDto findCompilationById(@PathVariable Long compId) throws EntityNotFoundException {
        log.info("Request to display a collection with ID-{}", compId);
        return compilationService.findCompilationById(compId);
    }

    @GetMapping
    List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                          @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request to display a list of collections with pinned-{}", pinned);
        return compilationService.findCompilations(pinned, from, size);
    }
}

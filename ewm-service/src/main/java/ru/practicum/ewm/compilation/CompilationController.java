package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;

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
    CompilationDto findCompilationById(@PathVariable Long compId) {
        log.info("Запрос на вывод подборки с ID-{}", compId);
        return compilationService.findCompilationById(compId);
    }

    @GetMapping
    List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на вывод списка подборок с pinned-{}", pinned);
        return compilationService.findCompilations(pinned, from, size);
    }
}

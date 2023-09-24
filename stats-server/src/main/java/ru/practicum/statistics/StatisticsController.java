package ru.practicum.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.dto.ViewStats;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.service.StatisticsServiceImpl;
import ru.practicum.statistics.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsServiceImpl service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto addHit(@RequestBody Hit hit) {
        log.info("Запрос на добавление записи в статистику: {}", hit);
        return service.addHit(hit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStatistics(@RequestParam(name = "start")
                                      @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime start,
                                         @RequestParam(name = "end")
                                      @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime end,
                                         @RequestParam(name = "uris", required = false) String[] uris,
                                         @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Запрос на вывод статистики по посещениям: start-{}; end-{}; uriList-{}", start, end, uris);
        return service.getStatistics(start, end, uris, unique);
    }
}
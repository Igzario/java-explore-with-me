package ru.practicum.statistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.service.StatisticsServiceImpl;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsServiceImpl service;

    @PostMapping("/hit")
    public void addHit(@RequestBody Hit hit) {
        log.info("Запрос на добавление записи в статистику: {}",hit);
        service.save(hit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<HitDto> getStatistics(@RequestParam(name = "start") String start,
                                      @RequestParam(name = "end") String end,
                                      @RequestParam(name = "uriList", required = false) List<String> uriList,
                                      @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Запрос на вывод статистики по посещениям: start-{}; end-{}; uriList-{}", start, end, uriList);
        return service.get(start, end, uriList, unique);
    }
}
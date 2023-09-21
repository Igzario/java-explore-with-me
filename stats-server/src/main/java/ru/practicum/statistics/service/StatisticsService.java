package ru.practicum.statistics.service;

import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    HitDto addHit(Hit hit);

    List<HitDto> getStatistics(LocalDateTime start, LocalDateTime end, String[] uriArray, boolean unique);
}
package ru.practicum.statistics.service;

import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.util.List;

public interface StatisticsService {

    void save(Hit hit);

    List<HitDto> get(String start, String end, List<String> uriList, boolean unique);
}
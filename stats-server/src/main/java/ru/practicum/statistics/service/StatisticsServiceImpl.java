package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.mapper.HitMapper;
import ru.practicum.statistics.repository.StatisticRepository;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticRepository statisticRepository;

    @Transactional
    @Override
    public HitDto addHit(Hit hit) {
        Hit hitAfterSave = statisticRepository.save(hit);
        log.info("Запись статистики сохранена: {}", hitAfterSave);
        return HitMapper.toHitDto(hitAfterSave);
    }

    @Override
    public List<HitDto> getStatistics(LocalDateTime startTime, LocalDateTime endTime, String[] uriArray, boolean unique) {
        List<HitDto> list;

        if (unique) {
            list = statisticRepository.countTotalperUriUniqueIp(startTime, endTime);

        } else {
            list = statisticRepository.countTotalperUri(startTime, endTime);
        }

        if (uriArray.length != 0) {
            List<String> urisList = List.of(uriArray);
            list = list.stream().filter(v -> urisList.contains(v.getUri())).
                    collect(Collectors.toList());
        }

        log.info("Выведен список Hits: {}", list);
        return list;
    }
}
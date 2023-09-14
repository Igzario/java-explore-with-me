package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.repository.StatisticRepository;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticRepository statisticRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public void save(Hit hit) {
        Hit hitAfterSave = statisticRepository.save(hit);
        log.info("Запись статистики сохранена: {}", hitAfterSave);
    }

    @Override
    public List<HitDto> get(String start, String end, List<String> uriList, boolean unique) {
        List<Object> list = null;
        List<HitDto> listHits = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endtTime = LocalDateTime.parse(end, formatter);
        if (unique) {
            list = statisticRepository.findAllHitsWithUniqueIp(LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        } else {
            list = statisticRepository.findAllHits(startTime,
                    endtTime);
        }
        list.forEach(obj -> {
            Object[] array = (Object[]) obj;
            HitDto hitDtoWithStat = new HitDto();
            hitDtoWithStat.setApp((String) array[0]);
            hitDtoWithStat.setUri((String) array[1]);
            hitDtoWithStat.setHits((BigInteger) array[2]);
            listHits.add(hitDtoWithStat);
        });
        if (uriList != null) {
            for (HitDto hit : listHits) {
                if (!uriList.contains(hit.getUri())) {
                    listHits.remove(hit);
                }
            }
        }
        log.info("Выведен список Hits: {}", listHits);
        return listHits;
    }
}
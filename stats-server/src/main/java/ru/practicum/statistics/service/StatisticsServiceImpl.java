package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.mapper.HitMapper;
import ru.practicum.statistics.repository.StatisticRepository;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

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
        List<Object> list = null;
        List<HitDto> listHits = new ArrayList<>();
        List<HitDto> resultListHits = new ArrayList<>();
        if (unique) {
            list = statisticRepository.findAllHitsWithUniqueIp(startTime, endTime);
        } else {
            list = statisticRepository.findAllHits(startTime, endTime);
        }
        list.forEach(obj -> {
            Object[] array = (Object[]) obj;
            HitDto hitDtoWithStat = new HitDto();
            hitDtoWithStat.setApp((String) array[0]);
            hitDtoWithStat.setUri((String) array[1]);
            hitDtoWithStat.setHits((BigInteger) array[2]);
            listHits.add(hitDtoWithStat);
        });


        if (uriArray != null && listHits.size() > 0) {
            List<String> uriList = new ArrayList<>(Arrays.asList(uriArray));
            for (HitDto hitDto : listHits) {
                if (uriList.contains(hitDto.getUri())) {
                    resultListHits.add(hitDto);
                }
            }
        }
        Collections.sort(resultListHits, new Comparator<HitDto>() {
            @Override
            public int compare(HitDto o1, HitDto o2) {
                return o2.getHits().compareTo(o1.getHits());
            }
        });
        log.info("Выведен список Hits: {}", resultListHits);
        return resultListHits;
    }
}
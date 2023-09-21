package ru.practicum.statistics.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

@UtilityClass
public class HitMapper {
    public HitDto toHitDto(Hit hit) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hitDto.getUri());
        return hitDto;
    }
}
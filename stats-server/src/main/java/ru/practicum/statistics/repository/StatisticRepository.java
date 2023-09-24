package ru.practicum.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics.dto.HitDto;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits " +
            "FROM hits AS e " +
            "WHERE e.timestamp between ?1 AND ?2 " +
            "GROUP BY e.uri", nativeQuery = true)
    List<HitDto> countTotalperUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits " +
            "FROM (SELECT DISTINCT ip AS i, * " +
            "FROM hits) AS e " +
            "WHERE e.timestamp between ?1 AND ?2 " +
            "GROUP BY e.uri", nativeQuery = true)
    List<HitDto> countTotalperUriUniqueIp(LocalDateTime start, LocalDateTime end);

}
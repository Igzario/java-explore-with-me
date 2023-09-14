package ru.practicum.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits FROM hits AS e " +
            "WHERE e.timestamp between :start AND :end GROUP BY e.uri", nativeQuery = true)
    List<Object> findAllHits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits FROM (SELECT" +
            " DISTINCT ON (ip) ip AS i, * FROM hits ) AS e WHERE e.timestamp " +
            "between :start AND :end GROUP BY e.uri", nativeQuery = true)
    List<Object> findAllHitsWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
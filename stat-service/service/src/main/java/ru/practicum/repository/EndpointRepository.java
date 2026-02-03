package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.DTO.ViewStats;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.DTO.ViewStats(" +
            "it.app, it.uri, CASE WHEN ?3 = true THEN count(distinct(it.ip)) ELSE count(*) END ) from EndpointHit as it " +
            "where it.timestamp between ?1 and ?2 " +
            "group by it.app, it.uri")
    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime stop, boolean unique);


    @Query("select new ru.practicum.DTO.ViewStats(" +
            "it.app, it.uri, CASE WHEN ?4 = true THEN count(distinct(it.ip)) ELSE count(*) END ) from EndpointHit as it " +
            "where it.timestamp between ?1 and ?2 " +
            "and it.uri in ?3 " +
            "group by it.app, it.uri")
    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime stop, String[] uris, boolean unique);

}

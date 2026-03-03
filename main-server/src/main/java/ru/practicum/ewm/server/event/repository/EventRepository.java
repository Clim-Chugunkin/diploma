package ru.practicum.ewm.server.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.server.event.DTO.EventWithRequests;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.request.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);

    List<Event> findAllByIdIn(Set<Long> ids);

    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Long countByCategoryId(Long id);


    @Query("select new ru.practicum.ewm.server.event.DTO.EventWithRequests(" +
            "it, count(r)  ) from Event as it left join Request as r on (r.event = it.id and r.status = :status)" +
            "where (:users is null or it.initiator.id in :users) and " +
            "(:states is null or it.state in :states) and " +
            "(:categories is null or it.category.id in :categories) and " +
            "(cast(:rangeStart as timestamp) is null or it.eventDate > :rangeStart) and " +
            "(cast(:rangeEnd as timestamp) is null or it.eventDate < :rangeEnd) " +
            "group by it.id")
    List<EventWithRequests> findEventsByAdmin(@Param("users") Integer[] users,
                                              @Param("states") String[] states,
                                              @Param("categories") Integer[] categories,
                                              @Param("rangeStart") LocalDateTime start,
                                              @Param("rangeEnd") LocalDateTime end,
                                              @Param("status") Status status,
                                              Pageable pageable);

    @Query("select new ru.practicum.ewm.server.event.DTO.EventWithRequests(" +
            "it, count(r)  ) from Event as it left join Request as r on (r.event = it.id and r.status = :status)" +
            " where " +
            "(:text is null or  (LOWER(it.annotation) like LOWER(CONCAT('%',cast(:text as String),'%')) or LOWER(it.description) like LOWER(CONCAT('%',cast(:text as String),'%'))) ) and " +
            "(:categories is null or it.category.id in :categories) and " +
            "(:paid is null or it.paid = :paid) and " +
            "(cast(:rangeStart as timestamp) is null or it.eventDate > :rangeStart) and " +
            "(cast(:rangeEnd as timestamp) is null or it.eventDate < :rangeEnd)  " +
            "group by it.id " +
            "having case when :available = true then count(r) < it.participantLimit else true END")
    List<EventWithRequests> getAllWithFilters(@Param("text") String text,
                                              @Param("categories") Integer[] categories,
                                              @Param("paid") Boolean paid,
                                              @Param("rangeStart") LocalDateTime start,
                                              @Param("rangeEnd") LocalDateTime end,
                                              @Param("available") Boolean available,
                                              @Param("status") Status status, Pageable pageable);

    @Query("select new ru.practicum.ewm.server.event.DTO.EventWithRequests(" +
            "it, count(r)  ) from Event as it left join Request as r on (r.event = it.id and r.status = :status) " +
            "where it.id = :id " +
            "and it.state = :state " +
            "group by it.id")
    Optional<EventWithRequests> getEventById(@Param("id") Long id,
                                             @Param("status") Status status,
                                             @Param("state") State state);
}

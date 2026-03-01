package ru.practicum.ewm.server.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.server.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestor(Long userId);

    List<Request> findAllByEvent(Long eventId);

    List<Request> findByIdIn(List<Long> ids);

    @Query("select count(it) from Request it " +
            "where it.event = ?1")
    Long getAllRequest(Long event);

    Long countByEventAndRequestor(Long event, Long requestor);
}

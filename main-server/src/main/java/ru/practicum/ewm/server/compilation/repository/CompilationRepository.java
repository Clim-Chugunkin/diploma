package ru.practicum.ewm.server.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.server.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select it from Compilation as it " +
            "where (:pinned is null or it.pinned = :pinned)")
    List<Compilation> getAll(@Param("pinned") Boolean pinned, Pageable pageable);
}

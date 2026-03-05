package ru.practicum.ewm.server.compilation.service;

import ru.practicum.ewm.server.compilation.DTO.CompilationDto;
import ru.practicum.ewm.server.compilation.DTO.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long id);

    CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);
}

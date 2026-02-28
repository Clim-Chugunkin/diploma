package ru.practicum.ewm.server.compilation.mapper;

import ru.practicum.ewm.server.compilation.DTO.CompilationDto;
import ru.practicum.ewm.server.compilation.DTO.NewCompilationDto;
import ru.practicum.ewm.server.compilation.model.Compilation;

public class CompilationMapper {
    public static Compilation fromCompilationDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setEvents(newCompilationDto.getEvents());
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto fromCompilationToCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation joinCompilationWithDto(Compilation compilation, NewCompilationDto compilationDto) {
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(compilationDto.getEvents());
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }
        return compilation;
    }

}

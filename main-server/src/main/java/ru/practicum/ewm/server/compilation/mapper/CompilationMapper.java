package ru.practicum.ewm.server.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.compilation.DTO.CompilationDto;
import ru.practicum.ewm.server.compilation.DTO.NewCompilationDto;
import ru.practicum.ewm.server.compilation.model.Compilation;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(target = "events", ignore = true)
    CompilationDto toCompilationDto(Compilation compilation);
}

package ru.practicum.ewm.server.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.compilation.DTO.CompilationDto;
import ru.practicum.ewm.server.compilation.DTO.NewCompilationDto;
import ru.practicum.ewm.server.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.server.compilation.model.Compilation;
import ru.practicum.ewm.server.compilation.repository.CompilationRepository;
import ru.practicum.ewm.server.event.mapper.EventMapper;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.repository.EventRepository;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.exceptions.InvalidArgumentException;
import ru.practicum.ewm.server.utils.OffsetLimitRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() == null) newCompilationDto.setEvents(Set.of());

        //check that all events exists
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Set<Long> unExistedEvents = new HashSet<>(newCompilationDto.getEvents());
        unExistedEvents.removeAll(events.stream().map(Event::getId).toList());

        if (!unExistedEvents.isEmpty()) {
            throw new ConflictException("событий с id = " + unExistedEvents + " нет");
        }

        CompilationDto compilationDto = CompilationMapper.fromCompilationToCompilationDto(compilationRepository.save(CompilationMapper.fromCompilationDtoToCompilation(newCompilationDto)));
        compilationDto.setEvents(events.stream().map(EventMapper::fromEventToShortEvent).toList());
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long id) {
        compilationRepository.findById(id).orElseThrow(() -> new ConditionsNotMetException("Подборки с id = " + id + "нет"));
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto) {

        if ((compilationDto.getTitle() != null) && (compilationDto.getTitle().length() > 50)) {
            throw new InvalidArgumentException("title должно быть от 1 до 50 символов");
        }

        //check if compilation exists
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ConditionsNotMetException("Подборки с id = " + compId + "нет"));
        //check if new events exists
        List<Event> events;
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilationDto.getEvents());
            Set<Long> unExistedEvents = new HashSet<>(compilationDto.getEvents());
            unExistedEvents.removeAll(events.stream().map(Event::getId).toList());
            if (!unExistedEvents.isEmpty()) {
                throw new ConflictException("событий с id = " + unExistedEvents + " нет");
            }
        } else {
            events = eventRepository.findAllByIdIn(compilation.getEvents());
        }
        Compilation updatedCompilation = CompilationMapper.joinCompilationWithDto(compilation, compilationDto);
        CompilationDto result = CompilationMapper.fromCompilationToCompilationDto(compilationRepository.save(updatedCompilation));
        result.setEvents(events.stream()
                .map(EventMapper::fromEventToShortEvent)
                .toList());
        return result;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ConditionsNotMetException("подборки с id = " + compId + "нет"));
        CompilationDto compilationDto = CompilationMapper.fromCompilationToCompilationDto(compilation);
        compilationDto.setEvents(eventRepository.findAllByIdIn(compilation.getEvents()).stream()
                .map(EventMapper::fromEventToShortEvent).toList());
        return compilationDto;

    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(from, size);
        List<Compilation> compilations = compilationRepository.getAll(pinned, offsetLimitRequest);
        Set<Long> eventsId = compilations.stream()
                .flatMap(compilation -> compilation.getEvents().stream())
                .collect(Collectors.toSet());
        List<Event> events = eventRepository.findAllByIdIn(eventsId);
        return compilations.stream()
                .map(it -> {
                    CompilationDto compilationDto = CompilationMapper.fromCompilationToCompilationDto(it);
                    compilationDto.setEvents(events.stream().filter(event -> it.getEvents().contains(event.getId())).map(EventMapper::fromEventToShortEvent).toList());
                    return compilationDto;
                })
                .toList();
    }
}

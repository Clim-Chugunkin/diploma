package ru.practicum.ewm.server.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.category.model.Category;
import ru.practicum.ewm.server.category.repository.CategoryRepository;
import ru.practicum.ewm.server.event.DTO.*;
import ru.practicum.ewm.server.event.mapper.EventMapper;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.event.model.StateAction;
import ru.practicum.ewm.server.event.repository.EventRepository;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.exceptions.InvalidDateException;
import ru.practicum.ewm.server.request.model.Status;
import ru.practicum.ewm.server.user.model.User;
import ru.practicum.ewm.server.user.repository.UserRepository;
import ru.practicum.ewm.server.utils.OffsetLimitRequest;
import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.DTO.ViewStats;
import ru.practicum.stats.client.StatsClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm.server.utils.DateTimeFormat.formatter;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto addEvent(NewEventDto newEvent, Long userId) {
        //check user existence
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check category existence
        Category cat = categoryRepository.findById(newEvent.getCategory()).orElseThrow(() -> new ConditionsNotMetException("категории  с id = " + newEvent.getCategory() + " нет"));
        //check eventDate is correct
        Event event = eventMapper.toEvent(newEvent);
        event.setInitiator(user);
        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new InvalidDateException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() -> new ConditionsNotMetException("такого события нет"));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, int from, int size) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(from, size);
        return eventRepository.findAllByInitiator_Id(userId, offsetLimitRequest).stream()
                .map(eventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updatedEvent) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));


        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        event = joinEventWithUpdateEvent(event, updatedEvent);
        //check if user is initiator
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new InvalidDateException("пользователь с id = " + userId + "не является создателем события");
        }
        //изменить можно только отмененные события или события в состоянии ожидания модерации
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("изменить можно только отмененные события или события в состоянии ожидания модерации");
        }

        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new InvalidDateException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        //если пришел запрос на отмену публикации
        if (updatedEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
            if (event.getState() != State.PENDING) {
                throw new InvalidDateException("Нельзя отменить событие потому что он не в стадии PENDING");
            }
            event.setState(State.CANCELED);
        }

        //если пришел запрос на публикацию
        if (updatedEvent.getStateAction() == StateAction.SEND_TO_REVIEW) {
            if (event.getState() != State.CANCELED) {
                throw new InvalidDateException("Нельзя нельзя отправить на рассмотрение  событие потому что он не в стадии CANCELED");
            }
            event.setState(State.PENDING);
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventUserRequest updatedEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        event = joinEventWithUpdateEvent(event, updatedEvent);

        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new InvalidDateException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }

        //событие можно публиковать, только если оно в состоянии ожидания публикации
        if (updatedEvent.getStateAction() == StateAction.PUBLISH_EVENT) {
            //событие можно публиковать, только если оно в состоянии ожидания публикации
            if (event.getState() != State.PENDING) {
                throw new ConflictException("Нельзя опубликовать событие потому что он  в стадии " + event.getState());
            }
            //дата начала изменяемого события должна быть не ранее чем за час от даты публикации
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidDateException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации ");
            }
            //если все условия выполнены
            //меняем статус события на PUBLISHED и записываем время публикации
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());

        }
        if (updatedEvent.getStateAction() == StateAction.REJECT_EVENT) {
            //событие можно отклонить, только если оно еще не опубликовано
            if (event.getState() == State.PUBLISHED) {
                throw new ConflictException("событие можно отклонить, только если оно еще не опубликовано");
            }
            event.setState(State.CANCELED);
        }


        return eventMapper.toEventFullDto(eventRepository.save(event));
    }


    @Override
    public List<EventFullDto> findEvents(FilterDto filter) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(filter.getFrom(), filter.getSize());
        return eventRepository.findEventsByAdmin(filter.getUsers(), filter.getStates(),
                        filter.getCategories(), filter.getStart(), filter.getEnd(), Status.CONFIRMED, offsetLimitRequest).stream()
                .map(this::toEventFullDto)
                .toList();

    }

    @Override
    public List<EventShortDto> findEventsWithFilters(FilterDto filterDto, String ip, String uri) {
        StatsHitDTO endpoint = new StatsHitDTO();
        endpoint.setIp(ip);
        endpoint.setApp("ewm-main-service");
        endpoint.setUri(uri);
        endpoint.setTimestamp(LocalDateTime.now().format(formatter));
        statsClient.addEndpoint(endpoint);
        //проверка валидации
        if ((filterDto.getStart() != null) && (filterDto.getEnd() != null)) {
            if (filterDto.getStart().isAfter(filterDto.getEnd())) {
                throw new InvalidDateException("дата начала не должна быть позже даты конца");
            }
        }

        Pageable offsetLimitRequest = new OffsetLimitRequest(filterDto.getFrom(), filterDto.getSize());
        List<EventShortDto> list = eventRepository.getAllWithFilters(filterDto.getText(),
                        filterDto.getCategories(),
                        filterDto.getPaid(),
                        filterDto.getStart(),
                        filterDto.getEnd(),
                        filterDto.getOnlyAvailable(),
                        Status.CONFIRMED,
                        offsetLimitRequest).stream()
                .map(this::toEventShortDto)
                .toList();

        switch (filterDto.getSort()) {
            case "EVENT_DATE" -> {
                return list.stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).toList();
            }
            case "VIEWS" -> {
                return list.stream().sorted(Comparator.comparing(EventShortDto::getViews)).toList();
            }
            case null, default -> {
                return list;
            }
        }
    }

    @Override
    public EventFullDto getEventById(Long id, String ip, String uri) {
        StatsHitDTO endpoint = new StatsHitDTO();
        endpoint.setIp(ip);
        endpoint.setApp("ewm-main-service");
        endpoint.setUri(uri);
        endpoint.setTimestamp(LocalDateTime.now().format(formatter));
        statsClient.addEndpoint(endpoint);
        LocalDateTime now = LocalDateTime.now();
        List<ViewStats> stats = statsClient.getStatistic(now.minusDays(1).format(formatter), now.plusDays(1).format(formatter), new String[]{uri}, true);
        Long views = stats.stream()
                .filter(it -> it.getUri().equals(uri))
                .map(ViewStats::getHits)
                .findFirst()
                .orElse(0L);

        EventFullDto event = this.toEventFullDto(eventRepository.getEventById(id, Status.CONFIRMED, State.PUBLISHED)
                .orElseThrow(() -> new ConditionsNotMetException("события с id = " + id + " нет или оно не опубликовано")));

        event.setViews(views);
        return event;
    }

    private Event joinEventWithUpdateEvent(Event event, UpdateEventUserRequest request) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (request.getCategory() != null) {
            Category cat = new Category();
            cat.setId(request.getCategory());
            event.setCategory(cat);
        }
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), formatter));
        }
        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        return event;
    }

    private EventFullDto toEventFullDto(EventWithRequests eventWithRequests) {
        if (eventWithRequests == null) return null;
        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventWithRequests.getEvent());
        eventFullDto.setConfirmedRequests(eventWithRequests.getConfirmedRequests());
        return eventFullDto;
    }

    private EventShortDto toEventShortDto(EventWithRequests eventWithRequests) {
        if (eventWithRequests == null) return null;
        EventShortDto eventShortDto = eventMapper.toEventShortDto(eventWithRequests.getEvent());
        eventShortDto.setConfirmedRequests(eventWithRequests.getConfirmedRequests());
        return eventShortDto;
    }
}

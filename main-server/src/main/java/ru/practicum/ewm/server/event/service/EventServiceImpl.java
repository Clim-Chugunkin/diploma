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

    @Override
    public EventFullDto addEvent(NewEventDto newEvent, Long userId) {
        //check user existence
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check category existence
        Category cat = categoryRepository.findById(newEvent.getCategory()).orElseThrow(() -> new ConditionsNotMetException("категории  с id = " + newEvent.getCategory() + " нет"));
        //check eventDate is correct
        Event event = EventMapper.fromNewEventToEvent(newEvent, user, cat);
        System.out.println(event.getEventDate());
        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new InvalidDateException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        return EventMapper.fromEventToFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() -> new ConditionsNotMetException("такого события нет"));
        return EventMapper.fromEventToFullEvent(event);
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, int from, int size) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(from, size);
        return eventRepository.findAllByInitiator_Id(userId, offsetLimitRequest).stream()
                .map(EventMapper::fromEventToShortEvent)
                .toList();
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updatedEvent) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));


        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        event = EventMapper.joinEventWithUpdateEvent(event, updatedEvent);
        //check if user is initiator
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new InvalidDateException("пользователь с id = " + userId + "не является создателем события");
        }
        //изменить можно только отмененные события или события в состоянии ожидания модерации
        if (event.getState() == State.PUBLISHED) {
            throw new InvalidDateException("изменить можно только отмененные события или события в состоянии ожидания модерации");
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

        return EventMapper.fromEventToFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventUserRequest updatedEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        event = EventMapper.joinEventWithUpdateEvent(event, updatedEvent);

        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new InvalidDateException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }

        //событие можно публиковать, только если оно в состоянии ожидания публикации
        if (updatedEvent.getStateAction() == StateAction.PUBLISH_EVENT) {
            //событие можно публиковать, только если оно в состоянии ожидания публикации
            if (event.getState() != State.PENDING) {
                throw new InvalidDateException("Нельзя опубликовать событие потому что он не в стадии " + event.getState());
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
                throw new InvalidDateException("событие можно отклонить, только если оно еще не опубликовано");
            }
            event.setState(State.CANCELED);
        }


        return EventMapper.fromEventToFullEvent(eventRepository.save(event));
    }


    @Override
    public List<EventFullDto> findEvents(FilterDto filter) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(filter.getFrom(), filter.getSize());
        return eventRepository.findEventsByAdmin(filter.getUsers(), filter.getStates(),
                        filter.getCategories(), filter.getStart(), filter.getEnd(), Status.CONFIRMED, offsetLimitRequest);

    }

    @Override
    public List<EventShortDto> findEventsWithFilters(FilterDto filterDto, String ip, String uri) {
//        StatsHitDTO endpoint = new StatsHitDTO();
//        endpoint.setIp(ip);
//        endpoint.setApp("ewm-main-service");
//        endpoint.setUri(uri);
//        endpoint.setTimestamp(LocalDateTime.now().format(formatter));
//        statsClient.addEndpoint(endpoint);
        System.out.println(filterDto.getStart());
        Pageable offsetLimitRequest = new OffsetLimitRequest(filterDto.getFrom(), filterDto.getSize());
        List<EventShortDto> list = eventRepository.getAllWithFilters(filterDto.getText(),
                filterDto.getCategories(),
                filterDto.getPaid(),
                filterDto.getStart(),
                filterDto.getEnd(),
                filterDto.getOnlyAvailable(),
                Status.CONFIRMED,
                offsetLimitRequest);

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
    public EventShortDto getEventById(Long id, String ip, String uri) {
        StatsHitDTO endpoint = new StatsHitDTO();
        endpoint.setIp(ip);
        endpoint.setApp("ewm-main-service");
        endpoint.setUri(uri);
        endpoint.setTimestamp(LocalDateTime.now().format(formatter));
        statsClient.addEndpoint(endpoint);
        LocalDateTime now = LocalDateTime.now();
        List<ViewStats> stats = statsClient.getStatistic(now.minusDays(1).format(formatter), now.format(formatter), new String[]{uri}, false);
        Long views = stats.stream()
                .filter(it -> it.getUri().equals(uri))
                .map(ViewStats::getHits)
                .findFirst()
                .orElse(0L);

        EventShortDto event = eventRepository.getEventById(id, Status.CONFIRMED, State.PUBLISHED)
                .orElseThrow(() -> new ConditionsNotMetException("события с id = " + id + " нет или оно не опубликовано"));
        event.setViews(views);
        return event;
    }
}

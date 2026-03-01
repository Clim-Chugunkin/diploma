package ru.practicum.ewm.server.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.event.repository.EventRepository;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.exceptions.InvalidDateException;
import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateResult;
import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;
import ru.practicum.ewm.server.request.mapper.RequestMapper;
import ru.practicum.ewm.server.request.model.Request;
import ru.practicum.ewm.server.request.model.Status;
import ru.practicum.ewm.server.request.repository.RequestRepository;
import ru.practicum.ewm.server.user.model.User;
import ru.practicum.ewm.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check if event exists
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        //инициатор события не может добавить запрос на участие в своём событии
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }
        //нельзя участвовать в неопубликованном событии
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("нельзя участвовать в неопубликованном событии");
        }
        //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
        Long count = requestRepository.getAllRequest(eventId, Status.CONFIRMED);
        if (event.getParticipantLimit() - count <= 0) {
            throw new ConflictException("достигнут лимит по заявкам на данное событие");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        //если пользователь уже создал запрос на событие
        if (requestRepository.countByEventAndRequestor(eventId, userId) != 0) {
            throw new ConflictException("Пользователь уже создал запрос на данное событие");
        }
        request.setRequestor(userId);
        request.setEvent(eventId);
        return RequestMapper.fromRequestToRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check if request exists
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new ConditionsNotMetException("запроса с id = " + requestId + "нет"));
        //check that user is requestor
        if (!userId.equals(request.getRequestor())) {
            throw new InvalidDateException("пользователь с id = " + userId + "не является создателем запроса с id " + requestId);
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.fromRequestToRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(Long userId) {
        return requestRepository.findAllByRequestor(userId).stream()
                .map(RequestMapper::fromRequestToRequestDto)
                .toList();
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestByEvent(Long userId, Long eventId) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check if event exists
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        //check that user is initiator
        if (!userId.equals(event.getInitiator().getId())) {
            throw new InvalidDateException("пользователь с id = " + userId + "не является создателем события с id " + eventId);
        }
        return requestRepository.findAllByEvent(eventId).stream()
                .map(RequestMapper::fromRequestToRequestDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult changeStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequests) {
        //check if user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя с id = " + userId + " нет"));
        //check if event exists
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события с id = " + eventId + " нет"));
        //get all requests in requestIds
        List<Request> requests = requestRepository.findByIdIn(eventRequests.getRequestIds());
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (event.getParticipantLimit() == 0) {
            result.setRejectedRequests(requests.stream().map(RequestMapper::fromRequestToRequestDto).toList());
            return result;
        }
        //get all confirmed request of event
        Long count = requestRepository.getAllRequest(eventId, Status.CONFIRMED);
        //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        if (event.getParticipantLimit() - count <= 0) {
            throw new ConflictException("достигнут лимит по заявкам на данное событие");
        }
        //статус можно изменить только у заявок, находящихся в состоянии ожидания
        requests.forEach((it) -> {
            if (it.getStatus() != Status.PENDING) {
                throw new InvalidDateException("статус можно изменить только у заявок, находящихся в состоянии ожидания ");
            }
        });
        if (eventRequests.getStatus() == ru.practicum.ewm.server.request.DTO.Status.CONFIRMED) {
            //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
            for (int i = 0; i < requests.size(); i++) {
                if ((event.getParticipantLimit() - count > 0)) {
                    requests.get(i).setStatus(Status.CONFIRMED);
                    result.getConfirmedRequests().add(RequestMapper.fromRequestToRequestDto(requests.get(i)));
                } else {
                    requests.get(i).setStatus(Status.CANCELED);
                    result.getRejectedRequests().add(RequestMapper.fromRequestToRequestDto(requests.get(i)));
                }
                count++;
            }
        } else {
            for (int i = 0; i < requests.size(); i++) {
                requests.get(i).setStatus(Status.CANCELED);
                result.getRejectedRequests().add(RequestMapper.fromRequestToRequestDto(requests.get(i)));
            }
        }
        requestRepository.saveAll(requests);
        return result;
    }
}

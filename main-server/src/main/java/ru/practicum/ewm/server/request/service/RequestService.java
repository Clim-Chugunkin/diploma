package ru.practicum.ewm.server.request.service;

import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateResult;
import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);

    List<ParticipationRequestDto> getAllRequestByEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}

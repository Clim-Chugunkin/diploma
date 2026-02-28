package ru.practicum.ewm.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.user.model.User;
import ru.practicum.ewm.server.user.repository.UserRepository;
import ru.practicum.ewm.server.utils.OffsetLimitRequest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ConflictException("такой емаил уже есть");
        }

        User user = userRepository.save(newUser);
        log.info("Добавлен новый пользователь {}", user.getName());
        return user;
    }

    @Override
    public void removeUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ConditionsNotMetException("пользователь не найден"));
        userRepository.deleteById(id);
        log.info("Пользователь {} удален", user.getName());
    }

    @Override
    public List<User> getUsers(String[] ids, int from, int limit) {
        Pageable offsetLimitRequest = new OffsetLimitRequest(from, limit);
        if (ids == null) {
            return userRepository.findAll(offsetLimitRequest).getContent();
        }
        return userRepository.findByIdIn(Arrays.stream(ids).map(Long::valueOf).toList(), offsetLimitRequest);
    }
}

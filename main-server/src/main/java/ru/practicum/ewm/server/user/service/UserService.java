package ru.practicum.ewm.server.user.service;

import ru.practicum.ewm.server.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    void removeUser(Long id);

    List<User> getUsers(String[] ids, int from, int limit);
}

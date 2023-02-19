package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getFriends(int id);

    List<User> getAllUsers();

    User findUserById(int id);

    User createUser(User user);

    User changeUser(User user);

    void makeFriends(int id, int friendId);

    void breakFriends(int id, int friendId);

    List<User> getMutualFriends(int id, int otherId);

    void deleteUser(int id);
}

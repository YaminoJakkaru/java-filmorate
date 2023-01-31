package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    public List<User> getFriends(int id);

    void makeFriends(int id, int friendId);

    void breakFriends(int id, int friendId);

    List<User> getMutualFriends(int id, int otherId);
}

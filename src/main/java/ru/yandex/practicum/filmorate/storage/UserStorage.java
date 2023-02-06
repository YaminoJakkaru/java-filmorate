package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User findUserById(int id);

    User createUser(User user);

    User changeUser(User user);

    List<User> getFriends(int id);

    void makeFriends(int id, int friendId);

    void breakFriends(int id, int friendId);

    List<User> getMutualFriends(int id, int otherId);


}

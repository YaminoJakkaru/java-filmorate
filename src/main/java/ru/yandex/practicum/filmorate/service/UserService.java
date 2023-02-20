package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Stream;

public interface UserService {

    List<User> getFriends(int id);

    List<User> getAllUsers();

    User findUserById(int id);

    User createUser(User user);

    User changeUser(User user);

    void makeFriends(int id, int friendId);

    void breakFriends(int id, int friendId);

    List<User> getMutualFriends(int id, int otherId);

    List<Film> getRecommendFilms(int id);
}

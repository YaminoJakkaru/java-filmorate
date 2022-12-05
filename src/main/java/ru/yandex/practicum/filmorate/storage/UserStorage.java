package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
@Service
public interface UserStorage {

    public ArrayList<User> getAllUsers();

    public User getUser(int id);

    public User createUser(User user);

    public User changeUser(User user);

    public void deleteUser(int id);
}

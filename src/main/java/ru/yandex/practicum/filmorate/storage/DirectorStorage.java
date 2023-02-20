package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director createDirector(Director director);

    Director findDirectorById(int id);

    List<Director> getAllDirectors();

    Director changeDirector(Director director);

    void deleteDirector(int id);
}

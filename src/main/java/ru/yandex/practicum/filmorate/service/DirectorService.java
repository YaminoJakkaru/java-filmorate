package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    Director createDirector(Director director);

    Director findDirectorById(int id);

    List<Director> getAllDirectors();

    Director changeDirector(Director director);

    void deleteDirector(int id);
}

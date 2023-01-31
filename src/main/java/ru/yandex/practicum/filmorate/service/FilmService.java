package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getTopFilms(int count);
}

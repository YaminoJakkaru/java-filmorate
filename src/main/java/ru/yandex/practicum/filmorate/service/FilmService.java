package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getAllFilms();

    Film findFilmById(int id);

    Film createFilm(Film film);

    Film changeFilm(Film film);

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getTopFilms(int count);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    List<Film> getSearchedFilms(String searchQuery, String searchSource);
}

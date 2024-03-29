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

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getTopFilms(int count, String genreId, String year);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    List<Film> getSearchedFilms(String searchQuery, String searchSource);

    void deleteFilm(int id);

}

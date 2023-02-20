package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film findFilmById(int id);

    Film createFilm(Film film);

    Film changeFilm(Film film);

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getTopFilms(int count, String genreId, String year);

    void addFilmsGenre(int filmId, int genreId);

    void deleteFilmsGenre(int filmId, int genreId);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getRecommendFilms(int id);

    void addFilmsDirector(int filmId, int directorId);

    void deleteFilmsDirector(int filmId, int directorId);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    List<Film> getSearchedFilms(String searchQuery, String searchSource);

    void deleteFilm(int id);
}

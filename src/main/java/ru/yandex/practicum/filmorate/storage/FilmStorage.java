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

    List<Film> getTopFilms(int count);

    void addFilmsGenre(int filmId, int genreId);

    void deleteFilmsGenre(int filmId, int genreId);

    List<Film> getCommonFilms(int userId, int friendId);
}

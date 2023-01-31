package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

     List<Film> getAllFilms();

     Film findFilmById(int id);

     Film createFilm(Film film);

     Film changeFilm(Film film);


}

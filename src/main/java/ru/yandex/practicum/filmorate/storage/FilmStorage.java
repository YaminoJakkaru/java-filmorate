package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;
@Service
public interface FilmStorage {

    public ArrayList<Film> getAllFilms();

    public Film getFilm(int id);

    public Film createFilm(Film film);

    public Film changeFilm(Film film);

    public void deleteFilm(int id);
}

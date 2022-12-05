package ru.yandex.practicum.filmorate.storage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.id.Id;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Id id;
    private final FilmValidator filmValidator;
    private static final Logger log = LoggerFactory.getLogger(FilmStorage.class);

    @Autowired
    public InMemoryFilmStorage(Id id, FilmValidator filmValidator) {
        this.id = id;
        this.filmValidator = filmValidator;

    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new FilmNotFoundException();
    }

    @Override
    public Film createFilm(Film film) {
        if (filmValidator.validate(film)) {
            film.setId(id.getNewFilmId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм");
            return film;
        }
        log.warn("Валидация фильма не пройдена");
        throw new ValidationException();
    }

    @Override
    public Film changeFilm(Film film) {
        if (filmValidator.validate(film)) {
            if (!films.containsKey(film.getId())) {
                log.warn("Попытка изменить несуществующий фильм");
                throw new NullPointerException();
            }
            films.put(film.getId(), film);
            log.info("Фильм изменен");
            return film;
        }
        log.warn("Валидация фильма не пройдена");
        throw new ValidationException();
    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }
}

package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.util.List;

@Service
@Qualifier("FilmDbService")
public class FilmDbService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorageValidator userStorageValidator;

    private final FilmValidator filmValidator;
    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator,
                         FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.userStorageValidator = userStorageValidator;
        this.filmValidator = filmValidator;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    @Override
    public Film createFilm(Film film) {
        if (!filmValidator.validate(film)) {
            LOG.warn("Валидация фильма не пройдена");
            throw new ValidationException();
        }
        return filmStorage.createFilm(film);
    }

    @Override
    public Film changeFilm(Film film) {
        if (!filmValidator.validate(film)) {
            LOG.warn("Валидация фильма не пройдена");
            throw new ValidationException();
        }
        return filmStorage.changeFilm(film);
    }

    @Override
    public void addLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}
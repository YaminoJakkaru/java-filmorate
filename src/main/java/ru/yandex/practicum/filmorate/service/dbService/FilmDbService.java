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
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.util.List;

@Service
@Qualifier("FilmDbService")
public class FilmDbService implements FilmService {

    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;
    private final UserStorageValidator userStorageValidator;
    private final FilmValidator filmValidator;
    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);
    private static final int LIKE = 1;
    private static final int REMOVE = 1;
    private static final int ADD = 2;

    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("EventDbStorage")EventStorage eventStorage,
                         @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator,
                         FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
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
        eventStorage.createEvent(userId,id,LIKE,ADD);
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        eventStorage.createEvent(userId,id,LIKE,REMOVE);
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    @Override
    public List<Film> getDirectorFilms(int directorId, String sortBy) {
        return filmStorage.getDirectorFilms(directorId, sortBy);
    }

    @Override
    public List<Film> getSearchedFilms(String searchQuery, String searchSource) {
        return filmStorage.getSearchedFilms(searchQuery, searchSource);
    }

    @Override
    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }
}
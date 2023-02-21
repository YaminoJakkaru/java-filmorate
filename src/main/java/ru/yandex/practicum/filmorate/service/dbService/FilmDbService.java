package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EntityType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.rowMapper.FilmMapper;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorageValidator;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.util.List;

@Service
public class FilmDbService implements FilmService {

    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;
    private final UserStorageValidator userStorageValidator;
    private final FilmStorageValidator filmStorageValidator;
    private final FilmValidator filmValidator;
    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("EventDbStorage")EventStorage eventStorage,
                         @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator,
                         @Qualifier("FilmDbStorageValidator")FilmStorageValidator filmStorageValidator,
                         FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
        this.userStorageValidator = userStorageValidator;
        this.filmStorageValidator = filmStorageValidator;
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
        eventStorage.createEvent(userId,id, EntityType.FILM, EventType.LIKE, Operation.ADD);
        if (!filmStorageValidator.filmLikeValidate(id, userId)) {
            filmStorage.addLike(id, userId);
            return;
        }
        LOG.warn("Пользователь уже поставил лайк этому фильму");
    }

    @Override
    public void deleteLike(int id, int userId) {
        if (!userStorageValidator.userIdValidate(userId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        eventStorage.createEvent(userId,id, EntityType.FILM, EventType.LIKE, Operation.REMOVE);
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count, String genreId, String year) {
        return filmStorage.getTopFilms(count, genreId, year);
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

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
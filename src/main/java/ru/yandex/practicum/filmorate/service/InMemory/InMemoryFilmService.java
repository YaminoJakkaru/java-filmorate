package ru.yandex.practicum.filmorate.service.InMemory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("InMemoryFilmService")
public class InMemoryFilmService implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmService.class);

    @Qualifier("InMemoryFilmStorage")
    private final FilmStorage filmStorage;
    @Qualifier("InMemoryUserStorage")
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmService(@Qualifier("InMemoryFilmStorage")FilmStorage filmStorage, @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
        if (userStorage.findUserById(userId) != null) {
            filmStorage.findFilmById(id).addLikes(userId);
            log.info("Добавлен лайк");
            return;
        }
        throw new UserNotFoundException();
    }

    public void deleteLike(int id, int userId) {
        if (userStorage.findUserById(userId) != null) {
            filmStorage.findFilmById(id).deleteLikes(userId);
            log.info("Удален лайк");
            return;
        }
        throw new UserNotFoundException();
    }

    public List<Film> getTopFilms(int count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> (int) (film.getDuration() + film.getLikes().size() * -1));
        TreeSet<Film> topFilms = new TreeSet<>(comparator);
        topFilms.addAll(filmStorage.getAllFilms());
        return  topFilms.stream().limit(count).collect(Collectors.toList());
    }
}

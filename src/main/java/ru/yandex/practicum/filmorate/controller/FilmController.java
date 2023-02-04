package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.List;

@RestController
public class FilmController {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("FilmDbService")
    private final FilmService filmService;


    @Autowired

    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("FilmDbService") FilmService filmService) {

        this.filmStorage = filmStorage;
        this.filmService = filmService;

    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film change(@RequestBody Film film) {
        return filmStorage.changeFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }
}

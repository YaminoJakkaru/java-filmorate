package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {


    private final FilmStorage filmStorage;
    private final FilmService filmService;


    @Autowired
    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage, FilmService filmService) {

        this.filmStorage = filmStorage;
        this.filmService = filmService;

    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping()
    public Film change(@RequestBody Film film) {
        return filmStorage.changeFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }
}

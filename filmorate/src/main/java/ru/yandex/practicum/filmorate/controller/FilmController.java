package ru.yandex.practicum.filmorate.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.id.Id;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    FilmValidator filmValidator= new FilmValidator();
    Id id=new Id();

    @GetMapping("/films")
    public ArrayList<Film> users() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film create(@RequestBody @NotNull Film film) {
        if(filmValidator.validate(film)) {
            film.setId(id.getNewId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм");
            return film;
        }
        log.warn("Валидация фильма не пройдена");
        throw new ValidationException();
    }

    @PutMapping("/films")
    public Film change(@RequestBody @NotNull Film film) {
        if(filmValidator.validate(film)) {
            if(!films.containsKey(film.getId())){
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
}

package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmValidator {
    private static final int MAX_CHAR_IN_DESCRIPTION = 200;
    private static final LocalDate EARLIEST_DATE = LocalDate.parse("1895-12-21");

    public boolean validate(Film film) {
        return nameValidate(film.getName()) && descriptionValidate(film.getDescription()) &&
                releaseDateValidate(film.getReleaseDate()) && durationValidate(film.getDuration());
    }

    public boolean nameValidate(String name) {
        return name != null && !name.isBlank();
    }

    public boolean descriptionValidate(String description) {
        return description != null && description.length() <= MAX_CHAR_IN_DESCRIPTION;
    }

    public boolean releaseDateValidate(LocalDate releaseDate) {
        return releaseDate != null && !releaseDate.isBefore(EARLIEST_DATE);
    }

    public boolean durationValidate(long duration) {
        return duration > 0;
    }
}

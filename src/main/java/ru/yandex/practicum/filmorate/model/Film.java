package ru.yandex.practicum.filmorate.model;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Film {
    int id;
    String name;
    String description;
    private LocalDate releaseDate;
    private long duration;
}

package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Mpa mpa;
    private final List<Genre> genres = new ArrayList<>();
    private final Set<Integer> likes = new HashSet<>();
    private final List<Director> directors = new ArrayList<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public void deleteLike(int id) {
        likes.remove(id);
    }

    public void addGenres(Genre genre) {
        genres.add(genre);
    }

    public void deleteGenres(int genreId) {
        genres.remove(genreId);
    }

    public void addDirectors(Director director) {
        directors.add(director);
    }

    public void deleteDirectors(int directorId) {
        directors.remove(directorId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        return values;
    }


}
